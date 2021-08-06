package com.provider.controller;

import com.provider.dto.TariffDto;
import com.provider.security.JwtProvider;
import com.provider.service.TariffService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
//@ExtendWith(SpringExtension.class) //is used to register extensions for the annotated test class or test method.
@WebMvcTest(TariffController.class) //Annotation that can be used for a Spring MVC test that focuses only on Spring MVC components.
class TariffControllerTest {

    @Autowired //mock will replace any existing bean of the same type in the application context.
    TariffService tariffService;

    @MockBean
    JwtProvider jwtProvider;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getAll() throws Exception {
        List<TariffDto> tariffDtoList = new ArrayList<>();
        when(tariffService.getAll()).thenReturn(tariffDtoList);

        mockMvc.perform(get("/api/v1/tariffs")).andDo(print()).andExpect(status().isOk());
    }
}