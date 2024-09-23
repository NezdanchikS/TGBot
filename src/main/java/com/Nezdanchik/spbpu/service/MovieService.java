package com.Nezdanchik.spbpu.service;

import com.Nezdanchik.spbpu.model.FilmModel;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;

@Component
@RequiredArgsConstructor
public class MovieService {

    private final RestClient restClient;

    public String getMovie(String filmId, FilmModel model) throws IOException, ParseException {
        String body = restClient.get()
                .uri(URI.create("https://kinopoiskapiunofficial.tech/api/v2.2/films/" + filmId))
                .retrieve()
                .body(String.class);

        JSONObject jsonObject = new JSONObject(body);
        fillModel(model, jsonObject);


        return "All info about film" + model.getNameRu() + model.getRatingKinopoisk() + model.getYear() + model.getDescription();

    }

    private static void fillModel(FilmModel model, JSONObject jsonObject) {
        model.setNameRu(jsonObject.getString("nameRu"));
        model.setRatingKinopoisk(jsonObject.getDouble("ratingKinopoisk"));
        model.setYear(jsonObject.getInt("year"));
        model.setDescription(jsonObject.getString("description"));
    }
}
