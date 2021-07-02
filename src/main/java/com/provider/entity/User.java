package com.provider.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
@Embeddable
public class User extends BaseEntity {
    private String login;

    private String password;

    private String firstName;

    private String lastName;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<TariffUser> tariffUserList;

    @OneToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @OneToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status status;
}
