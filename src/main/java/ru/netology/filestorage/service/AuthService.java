package ru.netology.filestorage.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.netology.filestorage.model.dto.AuthRequest;
import ru.netology.filestorage.model.dto.AuthResponse;
import ru.netology.filestorage.model.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthResponse login(AuthRequest authRequest);

    String logout(HttpServletRequest request, HttpServletResponse response, String authTokenHeader);

    UserDetails loadUserByUsername(String username);

    User getByUsername(String username) throws UsernameNotFoundException;
}
