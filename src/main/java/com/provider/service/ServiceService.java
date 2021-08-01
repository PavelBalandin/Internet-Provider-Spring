package com.provider.service;

import com.provider.dto.ServiceDto;
import com.provider.entity.Service;

import java.util.List;

public interface ServiceService {
    List<ServiceDto> getAll();

    ServiceDto findById(Long id);

}
