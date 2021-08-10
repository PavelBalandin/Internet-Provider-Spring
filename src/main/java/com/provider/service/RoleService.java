package com.provider.service;

import com.provider.entity.Role;

public interface RoleService {
    Role findByName(String name);
}
