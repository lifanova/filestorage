package ru.netology.filestorage.service;

import ru.netology.filestorage.model.dto.AuthRequest;
import ru.netology.filestorage.model.dto.AuthResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthResponse login(AuthRequest authRequest);

    String logout(HttpServletRequest request, HttpServletResponse response, String authTokenHeader);
}
