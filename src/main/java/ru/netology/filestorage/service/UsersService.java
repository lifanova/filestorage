package ru.netology.filestorage.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface UsersService {
    UserDetails loadUserByUsername(String username);
}
