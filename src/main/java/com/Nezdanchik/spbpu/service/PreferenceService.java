package com.Nezdanchik.spbpu.service;

import com.Nezdanchik.spbpu.model.User;
import com.Nezdanchik.spbpu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Set;

//@Service
@RequiredArgsConstructor
public class PreferenceService {

    private final UserRepository userRepository;

    @Transactional
    public void addPreferences(User user, Set<String> genres, Set<String> actors, Integer releaseYear) {
        if (genres != null) {
            user.getGenres().addAll(genres);
        }
        if (actors != null) {
            user.getActors().addAll(actors);
        }
        if (releaseYear != null) {
            user.setReleaseYear(releaseYear);
        }
        userRepository.save(user);
    }

    @Transactional
    public void deletePreferences(User user, Set<String> genres, Set<String> actors, Integer releaseYear) {
        if (genres != null) {
            user.getGenres().removeAll(genres);
        }
        if (actors != null) {
            user.getActors().removeAll(actors);
        }
        if (releaseYear != null) {
            user.setReleaseYear(null);
        }
        userRepository.save(user);
    }

    public String showPreferences(User user) {
        StringBuilder sb = new StringBuilder("Ваши предпочтения:\n");
        sb.append("Жанры: ").append(user.getGenres().isEmpty() ? "Не установлены" : String.join(", ", user.getGenres())).append("\n");
        sb.append("Актеры: ").append(user.getActors().isEmpty() ? "Не установлены" : String.join(", ", user.getActors())).append("\n");
        sb.append("Год выпуска: ").append(user.getReleaseYear() == null ? "Не установлен" : user.getReleaseYear());
        return sb.toString();
    }
}
