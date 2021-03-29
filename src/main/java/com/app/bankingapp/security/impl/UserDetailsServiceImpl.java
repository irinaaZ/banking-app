package com.app.bankingapp.security.impl;

import com.app.bankingapp.exceptions.ApplicationException;
import com.app.bankingapp.repositories.JpaUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private final JpaUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findUserByName(username)
                .map(user -> new User(
                        user.getName(),
                        user.getPassword(),
                        user.getRoles()
                                .stream()
                                .map(role -> new SimpleGrantedAuthority(role.getName()))
                                .collect(Collectors.toSet())))
                .orElseThrow(() -> new ApplicationException("User " + username + " is not found"));
    }
}
