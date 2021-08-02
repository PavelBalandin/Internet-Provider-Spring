package com.provider.controller;

import com.provider.dto.AuthRequest;
import com.provider.dto.RegistrationRequest;
import com.provider.dto.UserDto;
import com.provider.entity.User;
import com.provider.mapper.UserMapper;
import com.provider.security.JwtProvider;
import com.provider.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@CrossOrigin
@RestController
public class AuthController {

    private final UserService userService;

    private final JwtProvider jwtProvider;

    @Autowired
    public AuthController(UserService userService, JwtProvider jwtProvider, UserMapper userMapper) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "403"),
            @ApiResponse(responseCode = "409")})
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest) {
        log.trace("User registration");
        return new ResponseEntity<>(userService.signup(registrationRequest), HttpStatus.CREATED);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "403"),
            @ApiResponse(responseCode = "404")})
    @PostMapping("/auth")
    public ResponseEntity<Map<String, Object>> auth(@Valid @RequestBody AuthRequest request) {
        log.trace("User authorization");
        User userEntity = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        String token = jwtProvider.generateToken(userEntity);
        Map<String, Object> response = new HashMap<>();
        response.put("user", userEntity);
        response.put("token", token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
