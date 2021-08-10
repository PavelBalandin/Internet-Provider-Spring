package com.provider.service;

import com.provider.dto.RegistrationRequest;
import com.provider.dto.UserDto;
import com.provider.entity.Role;
import com.provider.entity.Status;
import com.provider.entity.User;
import com.provider.exception.ResourceNotFoundException;
import com.provider.exception.ResourcesAlreadyExistsException;
import com.provider.mapper.UserMapper;
import com.provider.repository.UserRepository;
import com.provider.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    RoleService roleService;
    @Mock
    StatusService statusService;
    @Mock
    PasswordEncoder passwordEncoder;

    UserService subject;

    @BeforeEach
    void setUp() {
        subject = new UserServiceImpl(userRepository, roleService, passwordEncoder, statusService, new UserMapper(new ModelMapper()));
    }

    @Test
    void getAll() {
        List<Role> roleList = new ArrayList<>();

        User user = User.builder()
                .id(1L)
                .login("login")
                .firstName("first")
                .lastName("last")
                .roleList(roleList)
                .status(new Status())
                .build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .login("login")
                .firstName("first")
                .lastName("last")
                .roleList(roleList)
                .status(new Status())
                .build();

        List<UserDto> usersExpected = new ArrayList<>();
        usersExpected.add(userDto);

        List<User> userList = new ArrayList<>();
        userList.add(user);
        when(userRepository.findAll()).thenReturn(userList);

        List<UserDto> usersActual = subject.getAll();

        assertEquals(usersExpected, usersActual);
    }

    @Test
    void findById() {
        UserDto userExpected = new UserDto();

        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto userActual = subject.findUserDtoById(1L);

        assertEquals(userExpected, userActual);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundById() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> subject.findUserDtoById(1L));
    }

    @Test
    void findByLogin() {
        UserDto userExpected = new UserDto();

        User user = new User();
        when(userRepository.findByLogin(any(String.class))).thenReturn(Optional.of(user));

        UserDto userActual = subject.findByLogin("login");

        assertEquals(userExpected, userActual);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundByLogin() {
        when(userRepository.findByLogin(any(String.class))).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> subject.findByLogin("invalidLogin"));
    }

    @Test
    void findByLoginAndPassword() {
        User userExpected = User.builder().password("password").build();
        when(userRepository.findByLogin(any(String.class))).thenReturn(Optional.of(userExpected));
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);

        User userActual = subject.findByLoginAndPassword("login", "password");

        assertEquals(userExpected, userActual);
    }

    @Test
    void shouldThrowExceptionWhenUserFindUserByLoginAndIncorrectPassword() {
        User userExpected = User.builder().password("passwordExpected").build();
        when(userRepository.findByLogin(any(String.class))).thenReturn(Optional.of(userExpected));
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> subject.findByLoginAndPassword("login", "password"));
    }

    @Test
    void create() {
        UserDto userExpected = new UserDto();
        RegistrationRequest registrationRequest = new RegistrationRequest();
        User user = new User();
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(roleService.findByName(any(String.class))).thenReturn(new Role());
        when(statusService.findByName(any(String.class))).thenReturn(new Status());

        UserDto userActual = subject.signup(registrationRequest);

        assertEquals(userExpected, userActual);
    }

    @Test
    void shouldThrowExceptionWhenTryCreateAlreadyExistEntity() {
        RegistrationRequest registrationRequest = new RegistrationRequest();

        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);
        when(roleService.findByName(any(String.class))).thenReturn(new Role());
        when(statusService.findByName(any(String.class))).thenReturn(new Status());

        assertThrows(ResourcesAlreadyExistsException.class, () -> subject.signup(registrationRequest));
    }

    @Test
    void update() {
        List<Role> roleList = new ArrayList<>();

        User user = User.builder()
                .id(1L)
                .login("login")
                .firstName("first")
                .lastName("last")
                .roleList(roleList)
                .status(new Status())
                .build();

        User userUpdated = User.builder()
                .id(1L)
                .login("login")
                .firstName("firstUpdated")
                .lastName("lastUpdated")
                .roleList(roleList)
                .status(new Status())
                .build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .login("login")
                .firstName("first")
                .lastName("last")
                .roleList(roleList)
                .status(new Status())
                .build();

        UserDto userExpected = UserDto.builder()
                .id(1L)
                .login("login")
                .firstName("firstUpdated")
                .lastName("lastUpdated")
                .roleList(roleList)
                .status(new Status())
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(userUpdated);

        UserDto userActual = subject.update(userDto, 1L);

        assertEquals(userExpected, userActual);
    }

    @Test
    void delete() {
        User user = User.builder().id(1L).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        subject.delete(1L);

        verify(userRepository, times(1)).delete(user);
    }
}
