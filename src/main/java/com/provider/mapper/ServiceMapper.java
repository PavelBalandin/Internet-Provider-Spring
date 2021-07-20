package com.provider.mapper;

import com.provider.dto.ServiceDTO;
import com.provider.entity.Service;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceMapper {
    public ServiceDTO entityToDTO(Service service) {
        ServiceDTO serviceDto = new ServiceDTO();
        serviceDto.setId(service.getId());
        serviceDto.setName(service.getName());
        return serviceDto;
    }

    public Service DTOtoEntity(ServiceDTO serviceDto) {
        Service service = new Service();
        service.setId(serviceDto.getId());
        service.setName(serviceDto.getName());
        return service;
    }

    public List<ServiceDTO> listEntityToDTOList(List<Service> serviceList) {
        return serviceList.stream().map(this::entityToDTO).collect(Collectors.toList());
    }
}
