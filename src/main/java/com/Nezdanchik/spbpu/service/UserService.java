package com.Nezdanchik.spbpu.service;

import com.Nezdanchik.spbpu.model.User;
import com.Nezdanchik.spbpu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


//@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User getOrCreateUser(Long chatId) {
        return userRepository.findById(chatId).orElseGet(() -> {
            User user = new User();
            user.setChatId(chatId);
            return userRepository.save(user);
        });
    }

    // Методы для управления предпочтениями
}
