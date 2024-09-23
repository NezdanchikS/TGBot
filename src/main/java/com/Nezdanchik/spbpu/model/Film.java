package com.Nezdanchik.spbpu.model;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
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
