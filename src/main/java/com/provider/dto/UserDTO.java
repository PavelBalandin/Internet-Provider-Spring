package com.provider.dto;

import com.provider.entity.Role;
import com.provider.entity.Status;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {

    private Long id;

    private String login;

    private String firstName;

    private String lastName;

    private List<Role> roleList;

    private Status status;
}
