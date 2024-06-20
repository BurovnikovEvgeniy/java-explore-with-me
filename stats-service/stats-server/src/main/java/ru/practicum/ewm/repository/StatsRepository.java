package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.ViewStats;
import ru.practicum.ewm.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stat, Long> {
    @Query("SELECT new ru.practicum.ewm.ViewStats(s.app, s.uri, COUNT(s.uri)) " +
            "FROM Stat s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.uri) DESC")
    List<ViewStats> findAllStats(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.ViewStats(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM Stat s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<ViewStats> findAllStatsUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.ViewStats(s.app, s.uri, COUNT(s.uri)) " +
            "FROM Stat s " +
            "WHERE s.uri IN :uris AND s.timestamp BETWEEN :start AND :end " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.uri) DESC")
    List<ViewStats> findAllStatsUrisIn(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.ViewStats(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM Stat s " +
            "WHERE s.uri LIKE :uris AND s.timestamp BETWEEN :start AND :end " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<ViewStats> findAllStatsUniqueIpUrisIn(String uris, LocalDateTime start, LocalDateTime end);
}
