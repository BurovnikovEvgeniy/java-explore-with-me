package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.EndpointHit;
import ru.practicum.ewm.ViewStats;
import ru.practicum.ewm.mapper.StatMapper;
import ru.practicum.ewm.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statRepository;
    private final StatMapper mapper;

    @Override
    @Transactional
    public EndpointHit saveHit(EndpointHit hit) {
        return mapper.toEndpointHit(statRepository.save(mapper.toStat(hit)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewStats> getViewStatsList(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (uris == null) {
            if (unique) {
                return statRepository.findAllUniqueStats(start, end);
            } else {
                return statRepository.findAllStats(start, end);
            }
        } else {
            if (unique) {
                return statRepository.findAllUniqueStatsWithUris(uris, start, end);
            } else {
                return statRepository.findAllStatsWithUris(uris, start, end);
            }
        }
    }
}