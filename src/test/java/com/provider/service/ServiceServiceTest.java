package com.provider.service;

import com.provider.dto.ServiceDto;
import com.provider.entity.Service;
import com.provider.mapper.ServiceMapper;
import com.provider.repository.ServiceRepository;
import com.provider.service.impl.ServiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceServiceTest {

    @Mock
    ServiceRepository serviceRepository;

    ServiceService subject;

    @BeforeEach
    void setUp() {
        subject = new ServiceServiceImpl(serviceRepository, new ServiceMapper(new ModelMapper()));
    }

    @Test
    void findAll() {
        List<ServiceDto> servicesExpected = new ArrayList<>();
        servicesExpected.add(ServiceDto.builder().id(1L).name("Service").build());

        List<Service> serviceList = new ArrayList<>();
        serviceList.add(Service.builder().id(1L).name("Service").build());

        when(serviceRepository.findAll()).thenReturn(serviceList);

        List<ServiceDto> servicesActual = subject.getAll();

        assertEquals(servicesExpected, servicesActual);
    }
}
