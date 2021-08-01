package com.provider.controller;

import com.provider.dto.UserDto;
import com.provider.entity.User;
import com.provider.mapper.UserMapper;
import com.provider.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@CrossOrigin
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "403")})
    public ResponseEntity<List<UserDto>> getAll() {
        log.trace("Getting user list");
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{login}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "403"),
            @ApiResponse(responseCode = "404")})
    public ResponseEntity<UserDto> getByLogin(@PathVariable String login) {
        log.trace("Getting user by login");
        return new ResponseEntity<>(userService.findByLogin(login), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "403"),
            @ApiResponse(responseCode = "404")})
    public ResponseEntity<UserDto> update(@Valid @RequestBody UserDto userDto, @PathVariable("id") Long id) {
        log.trace("Updating user by login");
        return new ResponseEntity<>(userService.update(userDto, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "403"),
            @ApiResponse(responseCode = "404")})
    public ResponseEntity<User> delete(@PathVariable("id") Long id) {
        log.trace("Deleting user by login");
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
