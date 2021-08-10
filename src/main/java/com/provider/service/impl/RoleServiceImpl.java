package com.provider.service.impl;

import com.provider.entity.Role;
import com.provider.exception.ResourceNotFoundException;
import com.provider.repository.RoleRepository;
import com.provider.service.RoleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findByName(String name) {
        Role role = roleRepository.findByName(name).orElseThrow(ResourceNotFoundException::new);
        log.trace("Found role in DB: " + role.getName());
        return role;
    }
}
