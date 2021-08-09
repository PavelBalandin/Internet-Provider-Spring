package com.provider.controller;

import com.provider.dto.ServiceDto;
import com.provider.security.JwtProvider;
import com.provider.service.ServiceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ServiceController.class)
class ServiceControllerTest {

    @MockBean
    ServiceService serviceService;

    @MockBean
    JwtProvider jwtProvider;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getAll() throws Exception {
        List<ServiceDto> expected = new ArrayList<>();
        expected.add(ServiceDto.builder().id(1L).name("name").build());
        when(serviceService.getAll()).thenReturn(expected);

        mockMvc.perform(get("/api/v1/services"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.[0].id").value("1"))
                .andExpect(jsonPath("$.[0].name").value("name"));
    }
}