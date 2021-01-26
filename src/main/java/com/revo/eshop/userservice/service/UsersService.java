package com.revo.eshop.userservice.service;

import com.revo.eshop.userservice.shared.UserDto;

public interface UsersService {
    UserDto createUser(UserDto userDetails);
}
