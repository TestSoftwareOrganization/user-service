package com.revo.eshop.userservice.service;

import com.revo.eshop.userservice.shared.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsersService extends UserDetailsService {
    UserDto createUser(UserDto userDetails);

    UserDto getUserDetailsByEmail(String Email);

    UserDto getUserByUserId(String userId) throws Exception;
}
