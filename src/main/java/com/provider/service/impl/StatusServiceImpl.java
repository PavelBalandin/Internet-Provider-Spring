package com.provider.service.impl;

import com.provider.entity.Status;
import com.provider.exception.ResourceNotFoundException;
import com.provider.repository.StatusRepository;
import com.provider.service.StatusService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;

    @Autowired
    public StatusServiceImpl(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public Status findByName(String name) {
        Status status = statusRepository.findByName(name).orElseThrow(ResourceNotFoundException::new);
        log.trace("Found status in DB: " + status.getName());
        return status;
    }
}
