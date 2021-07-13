package com.provider.service;

import com.provider.entity.Service;

import java.util.List;

public interface ServiceService {
    List<Service> getAll();

    Service findById(Long id);

}
