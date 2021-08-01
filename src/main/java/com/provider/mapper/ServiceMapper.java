package com.provider.mapper;

import com.provider.dto.ServiceDto;
import com.provider.entity.Service;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceMapper {
    public ServiceDto toDto(Service service) {
        ServiceDto serviceDto = new ServiceDto();
        serviceDto.setId(service.getId());
        serviceDto.setName(service.getName());
        return serviceDto;
    }

    public Service toEntity(ServiceDto serviceDto) {
        Service service = new Service();
        service.setId(serviceDto.getId());
        service.setName(serviceDto.getName());
        return service;
    }

    public List<ServiceDto> toDtoList(List<Service> serviceList) {
        return serviceList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
