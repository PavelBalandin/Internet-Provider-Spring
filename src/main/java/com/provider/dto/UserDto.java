package com.provider.dto;

import com.provider.entity.Role;
import com.provider.entity.Status;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class UserDto {

    private Long id;

    @NotNull
    @Size(min = 2, max = 30)
    private String login;

    @NotNull
    @Size(min = 2, max = 30)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 30)
    private String lastName;

    private List<Role> roleList;

    private Status status;
}
