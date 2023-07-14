package ru.netology.filestorage.service.impl;

import org.springframework.stereotype.Service;
import ru.netology.filestorage.exception.ErrorInputData;
import ru.netology.filestorage.model.entity.User;
import ru.netology.filestorage.repository.UserRepository;
import ru.netology.filestorage.service.UsersService;

import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService {
    private UserRepository userRepository;

    public UsersServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getByUsername(String username) {
        Optional<User> entity = userRepository.findByName(username);
        if (entity.isEmpty()) {
            throw new ErrorInputData("Пользователь по запросу не найден!");
        }

        return entity.get();
    }
}
