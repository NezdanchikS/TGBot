package com.Nezdanchik.spbpu.service;

import com.Nezdanchik.spbpu.model.FilmModel;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.Scanner;

public class MovieService {

    public static String getMovie(String filmId, FilmModel model) throws IOException, ParseException {
        URL url = new URL("https://kinopoiskapiunofficial.tech/api/v2.2/films/" + filmId);
        Scanner scanner = new Scanner((InputStream) url.getContent());
        String result = "";
        while (scanner.hasNext()) {
            result += scanner.nextLine();
        }

        JSONObject jsonObject = new JSONObject(result);
        model.setNameRu(jsonObject.getString("nameRu"));
        model.setRatingKinopoisk(jsonObject.getDouble("ratingKinopoisk"));
        model.setYear(jsonObject.getInt("year"));
        model.setDescription(jsonObject.getString("description"));


        return "All info about film" + model.getNameRu() + model.getRatingKinopoisk() + model.getYear() + model.getDescription();

    }
}
