package com.revo.eshop.userservice.controllers;

import com.revo.eshop.userservice.domain.User;
import com.revo.eshop.userservice.domain.UserPrincipal;
import com.revo.eshop.userservice.exception.ExceptionHandling;
import com.revo.eshop.userservice.exception.domain.EmailExistException;
import com.revo.eshop.userservice.exception.domain.UserNotFoundException;
import com.revo.eshop.userservice.exception.domain.UsernameExistException;
import com.revo.eshop.userservice.security.JWTTokenProvider;
import com.revo.eshop.userservice.service.UsersService;
import com.revo.eshop.userservice.shared.UserDto;
import com.revo.eshop.userservice.ui.model.users.LoginRequestModel;
import com.revo.eshop.userservice.ui.model.users.create.CreateUserRequestModel;
import com.revo.eshop.userservice.ui.model.users.create.CreateUserResponseModel;
import com.revo.eshop.userservice.ui.model.users.get.UserResponseModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.revo.eshop.userservice.constants.SecurityConstants.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = {"/", "/users"})
public class UsersController extends ExceptionHandling {

    private Environment env;

    private UsersService usersService;

    private AuthenticationManager authenticationManager;

    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    public UsersController(Environment env, UsersService usersService, AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider) {
        this.env = env;
        this.usersService = usersService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/status/check")
    public String status() {
        return "working on " + env.getProperty("local.server.port") + ", with token = " + env.getProperty("token.secret");
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userDetails) throws UserNotFoundException, EmailExistException, UsernameExistException {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        userDto = usersService.register(userDto);
        CreateUserResponseModel responseModel = modelMapper.map(userDto, CreateUserResponseModel.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseModel);
    }

    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseModel> getUser(@PathVariable("userId") String userId) throws Exception {
        UserDto userDto = usersService.getUserByUserId(userId);
        UserResponseModel userResponseModel = new ModelMapper().map(userDto, UserResponseModel.class);

        return ResponseEntity.status(HttpStatus.OK).body(userResponseModel);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseModel> login(@RequestBody LoginRequestModel usechr) {
        authenticate(user.getUsername(), user.getPassword());
        User loginUser = usersService.findUserByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
         HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        UserResponseModel userResponseModel = new ModelMapper().map(loginUser, UserResponseModel.class);

        return new ResponseEntity<UserResponseModel>(userResponseModel, jwtHeader, OK);
    }

    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));

        return headers;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
