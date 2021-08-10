package com.provider.service;

import com.provider.dto.RegistrationRequest;
import com.provider.dto.UserDto;
import com.provider.entity.User;

import java.util.List;

public interface UserService {
    List<UserDto> getAll();

    UserDto findUserDtoById(Long id);

    User findUserById(Long id);

    UserDto update(UserDto entity, Long id);

    void delete(Long id);

    UserDto findByLogin(String login);

    User findByLoginAndPassword(String login, String password);

    UserDto signup(RegistrationRequest registrationRequest);
}
