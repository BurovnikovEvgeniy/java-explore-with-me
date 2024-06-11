package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.ViewStats;
import ru.practicum.ewm.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stat, Long> {

    List<ViewStats> findAllStats(LocalDateTime start, LocalDateTime end);

    List<ViewStats> findAllUniqueStats(LocalDateTime start, LocalDateTime end);

    List<ViewStats> findAllStatsWithUris( List<String> uris, LocalDateTime start, LocalDateTime end);

    List<ViewStats> findAllUniqueStatsWithUris(List<String> uris, LocalDateTime start, LocalDateTime end);
}