package com.provider.service.impl;

import com.provider.dto.ServiceDto;
import com.provider.entity.Service;
import com.provider.exception.ResourceNotFoundException;
import com.provider.mapper.ServiceMapper;
import com.provider.repository.ServiceRepository;
import com.provider.service.ServiceService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Log4j2
@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;

    private final ServiceMapper serviceMapper;

    @Autowired
    public ServiceServiceImpl(ServiceRepository serviceRepository, ServiceMapper serviceMapper) {
        this.serviceRepository = serviceRepository;
        this.serviceMapper = serviceMapper;
    }

    @Override
    public List<ServiceDto> getAll() {
        List<Service> serviceList = serviceRepository.findAll();
        log.trace("Service list has been fetched");
        return serviceMapper.toDtoList(serviceList);
    }

    @Override
    public ServiceDto findById(Long id) {
        Service service = serviceRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        log.trace("Service has been fetched");
        return serviceMapper.toDto(service);
    }
}
