package com.Nezdanchik.spbpu.repository;

import com.Nezdanchik.spbpu.model.WatchlistItem;
import com.Nezdanchik.spbpu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchlistRepository extends JpaRepository<WatchlistItem, Long> {
    List<WatchlistItem> findByUser(User user);
    Optional<WatchlistItem> findByIdAndUser(Long id, User user);
}
