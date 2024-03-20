package com.example.authservice;

import com.example.authservice.dto.User;
import com.example.authservice.dto.UserRegistration;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserClientImpl implements UserDetailsService {

    @Autowired
    private UserClient userClient;


    public Optional<User> findByEmailForCheck(String email) {
        return userClient.findByEmailForCheck(email);
    }

    public User createUser(UserRegistration regRequest) {
        return userClient.createUser(regRequest);
    }

    public User findByRefreshToken(String refreshToken) {
        return userClient.findByRefreshToken(refreshToken);
    }

    public void updateUser(User user) {
        userClient.updateUser(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userClient.findByEmailForCheck(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.get().getRole().name()));
        return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(), authorities);
    }
}
