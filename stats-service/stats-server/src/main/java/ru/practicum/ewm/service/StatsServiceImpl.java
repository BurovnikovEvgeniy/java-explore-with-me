package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.EndpointHit;
import ru.practicum.ewm.ViewStats;
import ru.practicum.ewm.mapper.StatMapper;
import ru.practicum.ewm.model.Stat;
import ru.practicum.ewm.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final StatMapper mapper;

    @Override
    @Transactional
    public EndpointHit addStat(EndpointHit hit) {
        Stat stat = statsRepository.save(mapper.toStat(hit));
        log.info("stat saved successfully");
        return mapper.toEndpointHit(stat);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (uris.isEmpty()) {
            if (unique) {
                log.info("getStats uris empty, unique = true");
                return statsRepository.findAllStatsUniqueIp(start, end);
            } else {
                log.info("getStats uris empty, unique = false");
                return statsRepository.findAllStats(start, end);
            }
        } else {
            if (unique) {
                log.info("getStats uris not empty, unique = true");
                return statsRepository.findAllStatsUniqueIpUrisIn(uris.get(0), start, end);
            } else {
                log.info("getStats uris not empty, unique = false");
                return statsRepository.findAllStatsUrisIn(uris, start, end);
            }
        }
    }
}