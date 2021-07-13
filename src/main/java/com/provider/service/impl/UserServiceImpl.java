package com.provider.service.impl;

import com.provider.entity.Role;
import com.provider.entity.Status;
import com.provider.entity.User;
import com.provider.exception.ResourceNotFoundException;
import com.provider.exception.ResourcesAlreadyExistsException;
import com.provider.repository.RoleRepository;
import com.provider.repository.StatusRepository;
import com.provider.repository.UserRepository;
import com.provider.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final StatusRepository statusRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, StatusRepository statusRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.statusRepository = statusRepository;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public User create(User user) {
        Role role = roleRepository.findByName("ROLE_USER").orElseThrow(ResourceNotFoundException::new);
        Status status = statusRepository.findByName("ENABLED").orElseThrow(ResourceNotFoundException::new);
        List<Role> roleList = new ArrayList<>();
        roleList.add(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleList(roleList);
        user.setStatus(status);
        try {
            return userRepository.save(user);
        } catch (Exception exception) {
            throw new ResourcesAlreadyExistsException();
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
        User user = userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        userRepository.delete(user);
    }

    @Override
    public User findByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(ResourceNotFoundException::new);
    }

    public User findByLoginAndPassword(String login, String password) {
        User user = findByLogin(login);
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        throw new AccessDeniedException("Incorrect login or password");
    }
}
