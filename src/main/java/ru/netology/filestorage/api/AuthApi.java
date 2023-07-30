package ru.netology.filestorage.api;

import org.springframework.http.ResponseEntity;
import ru.netology.filestorage.model.dto.AuthRequest;
import ru.netology.filestorage.model.dto.AuthResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthApi {
    ResponseEntity<AuthResponse> authenticate(AuthRequest authRequest);

    ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response, String authTokenHeader);
}
