package com.Nezdanchik.spbpu.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FilmModel {
    private Long id;
    private String nameRu;
    private Double ratingKinopoisk;
    private int year;
    private String description;
}

