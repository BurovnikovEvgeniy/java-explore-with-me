package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.ViewStats;
import ru.practicum.ewm.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stat, Integer> {

    @Query("SELECT new ru.practicum.ewm.ViewStats(s.app, s.uri, COUNT(s.uri)) " +
            "FROM Stat as s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.uri) DESC")
    List<ViewStats> findAllStats(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.ViewStats(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM Stat as s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "GROUP BY s.app, s.uri, s.ip " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<ViewStats> findAllUniqueStats(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.ViewStats(s.app, s.uri, COUNT(s.uri)) " +
            "FROM Stat as s " +
            "WHERE s.timestamp BETWEEN :start AND :end AND s.uri IN :uris " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.uri) DESC")
    List<ViewStats> findAllStatsWithUris(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.ViewStats(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM Stat as s " +
            "WHERE s.timestamp BETWEEN :start AND :end AND s.uri IN :uris " +
            "GROUP BY s.app, s.uri, s.ip " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<ViewStats> findAllUniqueStatsWithUris(List<String> uris, LocalDateTime start, LocalDateTime end);
}