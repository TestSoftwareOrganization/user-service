package com.revo.eshop.userservice.service;

import com.revo.eshop.userservice.data.UserEntity;
import com.revo.eshop.userservice.repositories.UsersRepository;
import com.revo.eshop.userservice.shared.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImp implements UsersService{

     UsersRepository usersRepository;

    //Constructor based dependency injection
    @Autowired
    public UserServiceImp(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDto createUser(UserDto userDetails) {
        userDetails.setUserId(UUID.randomUUID().toString());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
        userEntity.setEncryptedPassword("test");
        usersRepository.save(userEntity);
        UserDto returnedValue = modelMapper.map(userEntity,UserDto.class);

        return returnedValue;
    }
}
