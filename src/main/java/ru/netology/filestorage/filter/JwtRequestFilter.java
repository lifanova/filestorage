package ru.netology.filestorage.filter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.netology.filestorage.model.entity.User;
import ru.netology.filestorage.service.UsersService;
import ru.netology.filestorage.util.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final UsersService usersService;
    //private final JwtBlackListService jwtBlackListService;
    private final JwtUtil jwtUtil;

    public JwtRequestFilter(UsersService usersService, JwtUtil jwtUtil) {
        this.usersService = usersService;
//        this.jwtBlackListService = jwtBlackListService;
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


//        if (jwt != null && jwtBlackListService.isBlacklisted(jwt)) {
//            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return;
//        }

        if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = usersService.getByUsername(login);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
