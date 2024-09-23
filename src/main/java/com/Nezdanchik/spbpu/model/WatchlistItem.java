package com.Nezdanchik.spbpu.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "watchlist")
public class WatchlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long filmId;

    private Boolean watched = false;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private User user;
}
