package com.Nezdanchik.spbpu.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "films")
public class Film {

    @Id
    private Long id;

    private String title;

    @ElementCollection
    @CollectionTable(name = "film_genres", joinColumns = @JoinColumn(name = "film_id"))
    @Column(name = "genre")
    private Set<String> genres = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "film_actors", joinColumns = @JoinColumn(name = "film_id"))
    @Column(name = "actor")
    private Set<String> actors = new HashSet<>();

    private Integer releaseYear;

    private Double rating;

    // Поле для отслеживания просмотра пользователем будет в WatchlistItem
}
