package com.revo.eshop.userservice.service;

import com.revo.eshop.userservice.domain.User;
import com.revo.eshop.userservice.enumeration.Role;
import com.revo.eshop.userservice.domain.UserPrincipal;
import com.revo.eshop.userservice.exception.domain.EmailExistException;
import com.revo.eshop.userservice.exception.domain.UserNotFoundException;
import com.revo.eshop.userservice.exception.domain.UsernameExistException;
import com.revo.eshop.userservice.repositories.UserRepository;
import com.revo.eshop.userservice.shared.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;

import static com.revo.eshop.userservice.constants.UserImplConstant.*;
import static com.revo.eshop.userservice.enumeration.Role.ROLE_USER;

@Service
@Transactional
//@Qualifier("userDetailsService")
public class UserServiceImp implements UsersService {

    UserRepository usersRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder;

    Environment environment;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserServiceImp(UserRepository usersRepository, BCryptPasswordEncoder bCryptPasswordEncoder, Environment environment) {
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.environment = environment;
    }

    @Override
    public UserDto register(UserDto userDetails) throws UserNotFoundException, EmailExistException, UsernameExistException {
        validateNewUsernameAndEmail(null, userDetails.getUsername(), userDetails.getEmail());
        userDetails.setUserId(UUID.randomUUID().toString());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        User userEntity = modelMapper.map(userDetails, User.class);
        userEntity.setJoinDate(new Date());
        userEntity.setActive(true);
        userEntity.setNotLocked(true);
        userEntity.setRole(ROLE_USER.name());
        userEntity.setAuthorities(ROLE_USER.getAuthorities());
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
        usersRepository.save(userEntity);
        UserDto returnedValue = modelMapper.map(userEntity, UserDto.class);
        logger.info("New user was registered: " + returnedValue.getUsername() + " with email: " + returnedValue.getEmail());
        return returnedValue;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = usersRepository.findByUsername(username);

        if (userEntity == null) {
            logger.error("User not found - " + username);
            throw new UsernameNotFoundException(username);
        } else {
            userEntity.setLastLoginDateDisplay(userEntity.getLastLoginDate());
            userEntity.setLastLoginDate(new Date());
            usersRepository.save(userEntity);
            UserPrincipal userPrincipal = new UserPrincipal(userEntity);
            logger.info("User " + username + " is found.");

            return userPrincipal;
        }
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        User userEntity = usersRepository.findByEmail(email);
        if (userEntity == null) throw new UsernameNotFoundException(email);

        return new ModelMapper().map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        User userEntity = usersRepository.findByUserId(userId);
        if (userEntity == null) throw new UsernameNotFoundException("User not found");

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        return userDto;
    }

    @Override
    public User findUserByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    @Override
    public User findUserByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {

        User userByNewUsername = findUserByUsername(newUsername);
        User userByNewEmail = findUserByEmail(newEmail);
        if (currentUsername != null) {
            User currentUser = findUserByUsername(currentUsername);
            if (currentUser == null) {
                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
            }

            if (userByNewUsername != null && currentUser.getId() != userByNewUsername.getId()) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if (userByNewEmail != null && currentUser.getId() != userByNewEmail.getId()) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return currentUser;
        } else {
            if (userByNewUsername != null) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if (userByNewEmail != null) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return null;
        }
    }

}
