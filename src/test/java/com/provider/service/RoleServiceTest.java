package com.provider.service;

import com.provider.entity.Role;
import com.provider.exception.ResourceNotFoundException;
import com.provider.mapper.ServiceMapper;
import com.provider.repository.RoleRepository;
import com.provider.service.impl.RoleServiceImpl;
import com.provider.service.impl.ServiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    RoleRepository roleRepository;

    RoleService subject;

    @BeforeEach
    void setUp() {
        subject = new RoleServiceImpl(roleRepository);
    }

    @Test
    void findByName() {
        Role expected = new Role();

        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(expected));

        Role actual = subject.findByName("name");

        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowExceptionIfRoleNotFoundByName() {
        when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> subject.findByName("name"));
    }
}