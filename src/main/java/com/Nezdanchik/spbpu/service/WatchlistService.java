package com.Nezdanchik.spbpu.service;

import com.Nezdanchik.spbpu.model.WatchlistItem;
import com.Nezdanchik.spbpu.model.User;
import com.Nezdanchik.spbpu.repository.WatchlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//@Service
@RequiredArgsConstructor
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;

    @Transactional
    public void addToWatchlist(User user, Long filmId) {
        WatchlistItem item = new WatchlistItem();
        item.setFilmId(filmId);
        item.setUser(user);
        watchlistRepository.save(item);
    }

    @Transactional
    public void removeFromWatchlist(User user,Long filmId) {
        watchlistRepository.findByUser(user).stream()
                .filter(item -> item.getFilmId().equals(filmId))
                .findFirst()
                .ifPresent(watchlistRepository::delete);
    }

    @Transactional
    public void markWatched(User user, Long filmId) {
        watchlistRepository.findByUser(user).stream()
                .filter(item -> item.getFilmId().equals(filmId))
                .findFirst()
                .ifPresent(item -> {
                    item.setWatched(true);
                    watchlistRepository.save(item);
                });
    }

    public List<WatchlistItem> getWatchlist(User user) {
        return watchlistRepository.findByUser(user);
    }
}
