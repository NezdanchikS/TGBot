package com.Nezdanchik.spbpu.repository;

import com.Nezdanchik.spbpu.model.Film;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilmRepository extends JpaRepository<Film, Long> {

    List<Film> findByGenresInAndActorsInAndReleaseYear(
            List<String> genres, List<String> actors, Integer releaseYear);

    // Добавьте методы поиска с пропуском критериев
}
