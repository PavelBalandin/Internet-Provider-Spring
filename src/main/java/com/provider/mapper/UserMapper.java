package com.provider.mapper;

import com.provider.dto.RegistrationRequest;
import com.provider.dto.UserDto;
import com.provider.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMapper {
    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setLogin(user.getLogin());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setRoleList(user.getRoleList());
        userDto.setStatus(user.getStatus());

        return userDto;
    }

    public User toEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setLogin(userDto.getLogin());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setRoleList(userDto.getRoleList());
        user.setStatus(userDto.getStatus());

        return user;
    }

    public List<UserDto> toDtoList(List<User> userList) {
        return userList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public User registrationRequestToEntity(RegistrationRequest registrationRequest) {
        User user = new User();
        user.setLogin(registrationRequest.getLogin());
        user.setFirstName(registrationRequest.getFirstName());
        user.setLastName(registrationRequest.getLastName());
        user.setPassword(registrationRequest.getPassword());

        return user;
    }
}
