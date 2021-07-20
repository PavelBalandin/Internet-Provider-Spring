package com.provider.controller;

import com.provider.dto.UserDTO;
import com.provider.entity.User;
import com.provider.mapper.UserMapper;
import com.provider.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@CrossOrigin
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        log.trace("Getting user list");
        return new ResponseEntity<>(userMapper.listEntityToDTOList(userService.getAll()), HttpStatus.OK);
    }

    @GetMapping("/{login}")
    public ResponseEntity<UserDTO> getByLogin(@PathVariable String login) {
        log.trace("Getting user by login");
        return new ResponseEntity<>(userMapper.entityToDTO(userService.findByLogin(login)), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO userDTO, @PathVariable("id") Long id) {
        log.trace("Updating user by login");
        User userUpdated = userService.update(userMapper.DTOtoEntity(userDTO), id);
        return new ResponseEntity<>(userMapper.entityToDTO(userUpdated), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> delete(@PathVariable("id") Long id) {
        log.trace("Deleting user by login");
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
