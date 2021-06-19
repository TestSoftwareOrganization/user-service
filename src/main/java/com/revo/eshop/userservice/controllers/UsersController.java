package com.revo.eshop.userservice.controllers;

import com.revo.eshop.userservice.exception.ExceptionHandling;
import com.revo.eshop.userservice.service.UsersService;
import com.revo.eshop.userservice.shared.UserDto;
import com.revo.eshop.userservice.ui.model.users.create.CreateUserRequestModel;
import com.revo.eshop.userservice.ui.model.users.create.CreateUserResponseModel;
import com.revo.eshop.userservice.ui.model.users.get.UserResponseModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = { "/", "/users"})
public class UsersController extends ExceptionHandling {

    @Autowired
    private Environment env;

    @Autowired
    private UsersService usersService;

    @GetMapping("/status/check")
    public String status() {
        return "working on " + env.getProperty("local.server.port") + ", with token = " + env.getProperty("token.secret");
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userDetails) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        userDto = usersService.createUser(userDto);
        CreateUserResponseModel responseModel = modelMapper.map(userDto, CreateUserResponseModel.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseModel);
    }

    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseModel> getUser(@PathVariable("userId") String userId) throws Exception {
        UserDto userDto = usersService.getUserByUserId(userId);
        UserResponseModel userResponseModel = new ModelMapper().map(userDto, UserResponseModel.class);

        return ResponseEntity.status(HttpStatus.OK).body(userResponseModel);
    }
}
