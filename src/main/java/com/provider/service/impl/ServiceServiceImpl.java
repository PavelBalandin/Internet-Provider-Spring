package com.provider.service.impl;

import com.provider.entity.Service;
import com.provider.exception.ResourceNotFoundException;
import com.provider.repository.ServiceRepository;
import com.provider.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public List<Service> getAll() {
        return serviceRepository.findAll();
    }

    @Override
    public Service findById(Long id) {
        return serviceRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public Service findByName(String name) {
        return null;
    }

    @Override
    public Service create(Service entity) {
        return null;
    }

    @Override
    public Service update(Service entity, Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
