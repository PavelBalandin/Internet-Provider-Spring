package com.provider.service;

import java.util.List;

public interface BaseService<E> {
    List<E> getAll();

    E findById(Long id);

    E findByName(String name);

    E create(E entity);

    E update(E entity, Long id);

    void delete(Long id);
}
