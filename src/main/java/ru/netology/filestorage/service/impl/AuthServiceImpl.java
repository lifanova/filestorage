package ru.netology.filestorage.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import ru.netology.filestorage.exception.ErrorInputData;
import ru.netology.filestorage.model.dto.AuthRequest;
import ru.netology.filestorage.model.dto.AuthResponse;
import ru.netology.filestorage.service.AuthService;
import ru.netology.filestorage.service.UsersService;
import ru.netology.filestorage.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UsersService usersService;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UsersService usersService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.usersService = usersService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword()));
        final String jwt = jwtUtil.generateToken(authRequest.getLogin());
        return new AuthResponse(jwt);
    }

    @Override
    public String logout(HttpServletRequest request, HttpServletResponse response, String authTokenHeader) {
        String jwt = authTokenHeader.split(" ")[1];
//        JwtBlackListEntity jwtBlackListEntity = jwtBlackListService.saveInBlackList(jwt);
//        if (jwtBlackListEntity == null) return false;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new ErrorInputData("");
        }

        SecurityContextLogoutHandler contextLogoutHandler = new SecurityContextLogoutHandler();
        contextLogoutHandler.logout(request, response, auth);

        return "true";
    }
}
