package com.provider.controller;

import com.provider.dto.TariffDto;
import com.provider.security.JwtProvider;
import com.provider.service.TariffService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TariffController.class) //Annotation that can be used for a Spring MVC test that focuses only on Spring MVC components.
class TariffControllerTest {

    @MockBean //mock will replace any existing bean of the same type in the application context.
    TariffService tariffService;

    @MockBean
    JwtProvider jwtProvider;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getAll() throws Exception {
        List<TariffDto> expected = new ArrayList<>();
        expected.add(TariffDto.builder().id(1L).name("name").description("description").duration(30).price(BigDecimal.valueOf(100)).build());
        Page<TariffDto> tariffDtoPage = new PageImpl<TariffDto>(expected);
        when(tariffService.getAll(0, 5, "id", "ASC")).thenReturn(tariffDtoPage);

        mockMvc.perform(get("/api/v1/tariffs"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.content.[0].id").value("1"))
                .andExpect(jsonPath("$.content.[0].name").value("name"))
                .andExpect(jsonPath("$.content.[0].description").value("description"))
                .andExpect(jsonPath("$.content.[0].duration").value("30"))
                .andExpect(jsonPath("$.content.[0].price").value("100"));
    }
}