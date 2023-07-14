package ru.netology.filestorage.service;

import ru.netology.filestorage.model.entity.User;

public interface UsersService {
    User getByUsername(String username);
}
