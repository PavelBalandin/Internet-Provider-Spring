package com.provider;

import com.provider.entity.Service;
import com.provider.repository.ServiceRepository;
import com.provider.service.ServiceService;
import com.provider.service.impl.ServiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceServiceTest {

    @Mock
    ServiceRepository serviceRepository;

    ServiceService subject;

    @BeforeEach
    void setUp() {
        subject = new ServiceServiceImpl(serviceRepository);
    }

    @Test
    void findAll() {
        List<Service> servicesExpected = new ArrayList<>();
        when(serviceRepository.findAll()).thenReturn(servicesExpected);

        List<Service> servicesActual = subject.getAll();

        assertEquals(servicesExpected, servicesActual);
    }
}
