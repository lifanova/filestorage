package ru.netology.filestorage.filter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.netology.filestorage.service.AuthService;
import ru.netology.filestorage.util.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public JwtRequestFilter(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        final String authTokenHeader = httpServletRequest.getHeader("auth-token");

        String login = null;
        String jwt = null;

        if (authTokenHeader != null && authTokenHeader.startsWith("Bearer ")) {
            jwt = authTokenHeader.split(" ")[1];
            login = jwtUtil.extractUsername(jwt);
        }

        if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails user = authService.loadUserByUsername(login);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
