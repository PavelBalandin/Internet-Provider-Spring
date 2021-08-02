package com.provider.service.impl;

import com.provider.dto.RegistrationRequest;
import com.provider.dto.UserDto;
import com.provider.entity.Role;
import com.provider.entity.Status;
import com.provider.entity.User;
import com.provider.exception.ResourceNotFoundException;
import com.provider.exception.ResourcesAlreadyExistsException;
import com.provider.mapper.UserMapper;
import com.provider.repository.RoleRepository;
import com.provider.repository.StatusRepository;
import com.provider.repository.UserRepository;
import com.provider.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final StatusRepository statusRepository;

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, StatusRepository statusRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.statusRepository = statusRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDto> getAll() {
        List<User> userList = userRepository.findAll();
        log.trace("User list has been fetched");
        return userMapper.toDtoList(userList);
    }

    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        log.trace("User has been fetched by id: " + id);
        return userMapper.toDto(user);
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        Role role = roleRepository.findByName("ROLE_USER").orElseThrow(ResourceNotFoundException::new);
        Status status = statusRepository.findByName("ENABLED").orElseThrow(ResourceNotFoundException::new);
        List<Role> roleList = new ArrayList<>();
        roleList.add(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleList(roleList);
        user.setStatus(status);
        try {
            User createdUser = userRepository.save(user);
            log.info("User has been registered. Login: " + user.getLogin());
            return userMapper.toDto(createdUser);
        } catch (DataIntegrityViolationException exception) {
            throw new ResourcesAlreadyExistsException();
        }
    }

    @Override
    public UserDto update(UserDto userDto, Long id) {
        User userFromDb = userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        BeanUtils.copyProperties(userMapper.toEntity(userDto), userFromDb, "id", "login", "password");
        User user = userRepository.save(userFromDb);
        log.info("User has been updated. Login: " + user.getLogin());
        return userMapper.toDto(user);
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        log.info("User has been Deleted. Login: " + user.getLogin());
        userRepository.delete(user);
    }

    @Override
    public UserDto findByLogin(String login) {
        User user = userRepository.findByLogin(login).orElseThrow(ResourceNotFoundException::new);
        log.trace("User has been fetched by login: " + login);
        return userMapper.toDto(user);
    }

    public User findByLoginAndPassword(String login, String password) {
        User user = userRepository.findByLogin(login).orElseThrow(ResourceNotFoundException::new);
        log.trace("Find by login and password: " + login);
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        log.trace("Incorrect login or password");
        throw new AccessDeniedException("Incorrect login or password");
    }

    @Override
    public UserDto signup(RegistrationRequest registrationRequest) {
        log.trace("User sign");
        User user = userMapper.registrationRequestToEntity(registrationRequest);
        Role role = roleRepository.findByName("ROLE_USER").orElseThrow(ResourceNotFoundException::new);
        Status status = statusRepository.findByName("ENABLED").orElseThrow(ResourceNotFoundException::new);
        List<Role> roleList = new ArrayList<>();
        roleList.add(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleList(roleList);
        user.setStatus(status);
        try {
            User createdUser = userRepository.save(user);
            log.info("User has been registered. Login: " + user.getLogin());
            return userMapper.toDto(createdUser);
        } catch (DataIntegrityViolationException exception) {
            throw new ResourcesAlreadyExistsException();
        }
    }
}
