package com.revo.eshop.userservice.service;

import com.revo.eshop.userservice.domain.User;
import com.revo.eshop.userservice.exception.domain.EmailExistException;
import com.revo.eshop.userservice.exception.domain.UserNotFoundException;
import com.revo.eshop.userservice.exception.domain.UsernameExistException;
import com.revo.eshop.userservice.shared.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsersService extends UserDetailsService {
    UserDto register(UserDto userDetails) throws UserNotFoundException, EmailExistException, UsernameExistException;

    UserDto getUserDetailsByEmail(String Email);

    UserDto getUserByUserId(String userId) throws Exception;

    User findUserByUsername(String username);

    User findUserByEmail(String email);
}
