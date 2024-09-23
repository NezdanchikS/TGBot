package com.Nezdanchik.spbpu.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    private Long chatId; // Используем chatId как уникальный идентификатор пользователя

    @ElementCollection
    @CollectionTable(name = "user_preferences", joinColumns = @JoinColumn(name = "chat_id"))
    @Column(name = "preference")
    private Set<String> genres = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "user_actors", joinColumns = @JoinColumn(name = "chat_id"))
    @Column(name = "actor")
    private Set<String> actors = new HashSet<>();

    private Integer releaseYear;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WatchlistItem> watchlist = new HashSet<>();
}
