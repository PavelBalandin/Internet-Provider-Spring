package com.provider.mapper;

import com.provider.dto.RegistrationRequest;
import com.provider.dto.UserDTO;
import com.provider.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMapper {
    public UserDTO entityToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setLogin(user.getLogin());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setRoleList(user.getRoleList());
        userDTO.setStatus(user.getStatus());

        return userDTO;
    }

    public User DTOtoEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setLogin(userDTO.getLogin());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setRoleList(userDTO.getRoleList());
        user.setStatus(userDTO.getStatus());

        return user;
    }

    public List<UserDTO> listEntityToDTOList(List<User> userList) {
        return userList.stream().map(this::entityToDTO).collect(Collectors.toList());
    }

    public User RegistrationRequestToEntity(RegistrationRequest registrationRequest) {
        User user = new User();
        user.setLogin(registrationRequest.getLogin());
        user.setFirstName(registrationRequest.getFirstName());
        user.setLastName(registrationRequest.getLastName());
        user.setPassword(registrationRequest.getPassword());

        return user;
    }
}
