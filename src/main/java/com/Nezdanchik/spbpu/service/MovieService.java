package com.Nezdanchik.spbpu.service;

import com.Nezdanchik.spbpu.model.Film;
import com.Nezdanchik.spbpu.model.FilmModel;
import com.Nezdanchik.spbpu.model.User;
import com.Nezdanchik.spbpu.repository.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.List;
import java.util.Random;

//@Service
@RequiredArgsConstructor
public class MovieService {

    private final RestClient restClient;
    private final FilmRepository filmRepository;

    public String getMovie(String filmId, FilmModel model) throws IOException, ParseException {
        // Предполагается, что filmId — это уникальный идентификатор фильма
        String body = restClient.get()
                .uri(URI.create("https://kinopoiskapiunofficial.tech/api/v2.2/films/" + filmId))
                .retrieve()
                .body(String.class);

        JSONObject jsonObject = new JSONObject(body);
        fillModel(model, jsonObject);

        return "Информация о фильме:\n" +
                "Название: " + model.getNameRu() + "\n" +
                "Рейтинг: " + model.getRatingKinopoisk() + "\n" +
                "Год выпуска: " + model.getYear() + "\n" +
                "Описание: " + model.getDescription();
    }

    private static void fillModel(FilmModel model, JSONObject jsonObject) {
        model.setNameRu(jsonObject.getString("nameRu"));
        model.setRatingKinopoisk(jsonObject.getDouble("ratingKinopoisk"));
        model.setYear(jsonObject.getInt("year"));
        model.setDescription(jsonObject.getString("description"));
    }

    public List<Film> findFilms(User user, boolean skipGenre, boolean skipActors, boolean skipYear) {
        // Реализуйте поиск фильмов на основе предпочтений пользователя
        // Например, используя методы из FilmRepository

        // Пример простого поиска:
        List<String> genres = skipGenre ? null : List.copyOf(user.getGenres());
        List<String> actors = skipActors ? null : List.copyOf(user.getActors());
        Integer year = skipYear ? null : user.getReleaseYear();

        // Здесь нужно создать кастомный метод в FilmRepository для обработки возможных null значений
        // Для простоты, используем findAll и фильтруем в памяти (неэффективно для больших баз)
        List<Film> allFilms = filmRepository.findAll();
        return allFilms.stream()
                .filter(film -> skipGenre || !user.getGenres().isEmpty() && film.getGenres().stream().anyMatch(user.getGenres()::contains))
                .filter(film -> skipActors || !user.getActors().isEmpty() && film.getActors().stream().anyMatch(user.getActors()::contains))
                .filter(film -> skipYear || (user.getReleaseYear() != null && film.getReleaseYear().equals(user.getReleaseYear())))
                .toList();
    }

    public Film getRandomFilm(User user) {
        List<Film> films = findFilms(user, false, false, false);
        if (films.isEmpty()) return null;
        Random random = new Random();
        return films.get(random.nextInt(films.size()));
    }
}
