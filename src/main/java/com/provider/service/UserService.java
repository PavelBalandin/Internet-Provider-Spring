package com.provider.service;

import com.provider.entity.User;

public interface UserService extends BaseService<User> {
    User getByLogin(String login);
}
