package com.provider.service;

import com.provider.entity.User;

public interface UserService extends BaseService<User> {
    User findByLogin(String login);

    User findByLoginAndPassword(String login, String password);
}
