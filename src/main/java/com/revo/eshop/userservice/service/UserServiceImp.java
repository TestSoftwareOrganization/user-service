package com.revo.eshop.userservice.service;

import com.revo.eshop.userservice.data.UserEntity;
import com.revo.eshop.userservice.repositories.UsersRepository;
import com.revo.eshop.userservice.serviceClients.OrdersServiceClient;
import com.revo.eshop.userservice.shared.UserDto;
import com.revo.eshop.userservice.ui.model.orders.OrdersResponseModel;
import feign.FeignException;
import feign.RetryableException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImp implements UsersService {

    UsersRepository usersRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder;

    OrdersServiceClient ordersServiceClient;

    Environment environment;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    //Constructor based dependency injection
    @Autowired
    public UserServiceImp(UsersRepository usersRepository, BCryptPasswordEncoder bCryptPasswordEncoder, OrdersServiceClient ordersServiceClient, Environment environment) {
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.ordersServiceClient = ordersServiceClient;
        this.environment = environment;
    }

    @Override
    public UserDto createUser(UserDto userDetails) {
        userDetails.setUserId(UUID.randomUUID().toString());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
        usersRepository.save(userEntity);
        UserDto returnedValue = modelMapper.map(userEntity, UserDto.class);

        return returnedValue;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = usersRepository.findByEmail(username);
        if (userEntity == null) throw new UsernameNotFoundException(username);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(),
                true, true, true, true,
                new ArrayList<>());
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = usersRepository.findByEmail(email);
        if (userEntity == null) throw new UsernameNotFoundException(email);

        return new ModelMapper().map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = usersRepository.findByUserId(userId);
        if (userEntity == null) throw new UsernameNotFoundException("User not found");

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        List<OrdersResponseModel> ordersList = new ArrayList<>();

        logger.info("Before calling Orders Microservice...");
        try {
            ordersList = ordersServiceClient.getOrdersByUserId(userId);
        }catch (FeignException e){
            logger.info(e.getLocalizedMessage());
        }

        logger.info("After calling Orders Microservice...");
        userDto.setOrders(ordersList);

        return userDto;
    }
}
