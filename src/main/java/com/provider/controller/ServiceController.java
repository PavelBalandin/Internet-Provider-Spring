package com.provider.controller;

import com.provider.dto.ServiceDTO;
import com.provider.mapper.ServiceMapper;
import com.provider.service.ServiceService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@CrossOrigin
@RestController
@RequestMapping("api/v1/services")
public class ServiceController {

    private final ServiceService serviceService;

    private final ServiceMapper serviceMapper;

    @Autowired
    public ServiceController(ServiceService serviceService, ServiceMapper serviceMapper) {
        this.serviceService = serviceService;
        this.serviceMapper = serviceMapper;
    }

    @GetMapping
    public ResponseEntity<List<ServiceDTO>> getAll() {
        log.trace("Getting service list");
        return new ResponseEntity<>(serviceMapper.listEntityToDTOList(serviceService.getAll()), HttpStatus.OK);
    }

}
