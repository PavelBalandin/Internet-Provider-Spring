package com.provider.service;

import com.provider.entity.Status;
import com.provider.exception.ResourceNotFoundException;
import com.provider.repository.StatusRepository;
import com.provider.service.impl.StatusServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatusServiceTest {

    @Mock
    StatusRepository statusRepository;

    StatusService subject;

    @BeforeEach
    void setUp() {
        subject = new StatusServiceImpl(statusRepository);
    }

    @Test
    void findByName() {
        Status expected = new Status();

        when(statusRepository.findByName(anyString())).thenReturn(Optional.of(expected));

        Status actual = subject.findByName("name");

        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowExceptionIfStatusNotFoundByName() {
        when(statusRepository.findByName(anyString())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> subject.findByName("name"));
    }
}