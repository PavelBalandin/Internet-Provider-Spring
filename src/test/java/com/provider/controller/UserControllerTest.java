package com.provider.controller;

import com.provider.security.JwtProvider;
import com.provider.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @MockBean
    UserService userService;

    @MockBean
    JwtProvider jwtProvider;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getAll() {
    }

    @Test
    void getByLogin() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}