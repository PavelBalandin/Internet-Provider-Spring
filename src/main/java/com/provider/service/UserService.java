package com.provider.service;

import com.provider.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User findById(Long id);

    User create(User entity);

    User update(User entity, Long id);

    void delete(Long id);

    User findByLogin(String login);

    User findByLoginAndPassword(String login, String password);
}
