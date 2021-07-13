package com.provider;

import com.provider.service.ServiceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Sql(value = {"/create-tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/drop-tables.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ServiceServiceTest {

    @Autowired
    private ServiceService serviceService;

    @Test
    public void shouldReturnServiceList() {
        assertFalse(serviceService.getAll().isEmpty());
    }
}
