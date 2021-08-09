package com.provider.controller;

import com.provider.dto.TariffDto;
import com.provider.entity.Status;
import com.provider.entity.User;
import com.provider.mapper.UserMapper;
import com.provider.security.JwtProvider;
import com.provider.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @MockBean
    UserService userService;

    @MockBean
    JwtProvider jwtProvider;

    @MockBean
    UserMapper userMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    void registerUser() {
    }

    @Test
    @WithMockUser
    void auth() throws Exception {

    }
}