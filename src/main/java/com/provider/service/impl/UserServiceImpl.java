package com.provider.service.impl;

import com.provider.entity.User;
import com.provider.exception.EntityAlreadyExistsException;
import com.provider.exception.ResourceNotFoundException;
import com.provider.repository.UserRepository;
import com.provider.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired


    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public User findByName(String name) {
        return null;
    }

    @Override
    public User create(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception exception) {
            throw new EntityAlreadyExistsException();
        }
    }

    @Override
    public User update(User user, Long id) {
        User userFromDb = userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        BeanUtils.copyProperties(user, userFromDb, "id", "login", "password");
        return userRepository.save(userFromDb);
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public User getByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(ResourceNotFoundException::new);
    }
}
