package com.revo.eshop.userservice.ui.controllers;

import com.revo.eshop.userservice.service.UsersService;
import com.revo.eshop.userservice.shared.UserDto;
import com.revo.eshop.userservice.ui.models.CreateUserRequestModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private Environment env;

    @Autowired
    private UsersService usersService;

    @GetMapping("/status/check")
    public String status() {
        return "working on " + env.getProperty("local.server.port");
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public String createUser(@Valid @RequestBody CreateUserRequestModel userDetails) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        return "Create user";
    }
}
