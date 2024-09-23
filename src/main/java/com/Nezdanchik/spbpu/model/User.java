package com.Nezdanchik.spbpu.model;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
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
    @ToString.Exclude
    private Set<WatchlistItem> watchlist = new HashSet<>();

}
