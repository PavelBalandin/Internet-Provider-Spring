package com.provider.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "roleList", fetch = FetchType.LAZY)
    private List<User> userList;
}
