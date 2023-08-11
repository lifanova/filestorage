package ru.netology.filestorage.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.netology.filestorage.model.entity.User;

public interface UsersService {
    UserDetails loadUserByUsername(String username);

    User getByUsername(String username) throws UsernameNotFoundException;
}
