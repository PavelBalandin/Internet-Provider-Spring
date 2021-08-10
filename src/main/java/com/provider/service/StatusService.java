package com.provider.service;

import com.provider.entity.Status;

public interface StatusService {
    Status findByName(String name);
}
