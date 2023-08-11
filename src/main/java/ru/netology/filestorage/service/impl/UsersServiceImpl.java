package ru.netology.filestorage.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.netology.filestorage.exception.ErrorInputData;
import ru.netology.filestorage.model.authority.UserDetailsImpl;
import ru.netology.filestorage.model.entity.User;
import ru.netology.filestorage.repository.UserRepository;
import ru.netology.filestorage.service.UsersService;

import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService, UserDetailsService {
    private UserRepository userRepository;

    public UsersServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserDetailsImpl(getByUsername(username));
    }

    public User getByUsername(String username) throws UsernameNotFoundException {
        Optional<User> entity = userRepository.findByName(username);
        if (entity.isEmpty()) {
            throw new ErrorInputData("Пользователь по запросу не найден!");
        }

        return entity.get();
    }
}
