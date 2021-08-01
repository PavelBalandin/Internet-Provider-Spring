package com.provider.service.impl;

import com.provider.dto.ServiceDto;
import com.provider.entity.Service;
import com.provider.exception.ResourceNotFoundException;
import com.provider.mapper.ServiceMapper;
import com.provider.repository.ServiceRepository;
import com.provider.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
        return serviceMapper.toDtoList(serviceList);
    }

    @Override
    public ServiceDto findById(Long id) {
        Service service = serviceRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        return serviceMapper.toDto(service);
    }
}
