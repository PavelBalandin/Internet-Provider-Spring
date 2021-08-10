package com.provider.mapper;

import com.provider.dto.ServiceDto;
import com.provider.entity.Service;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public ServiceMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ServiceDto toDto(Service service) {
        return modelMapper.map(service, ServiceDto.class);
    }

    public Service toEntity(ServiceDto serviceDto) {
        return modelMapper.map(serviceDto, Service.class);
    }

    public List<ServiceDto> toDtoList(List<Service> serviceList) {
        return serviceList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
