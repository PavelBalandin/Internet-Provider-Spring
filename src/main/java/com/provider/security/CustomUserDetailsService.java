package com.provider.security;

import com.provider.entity.User;
import com.provider.exception.ResourceNotFoundException;
import com.provider.repository.UserRepository;
import com.provider.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login).orElseThrow(ResourceNotFoundException::new);
        return CustomUserDetails.fromUserToCustomUserDetails(user);
    }
}
