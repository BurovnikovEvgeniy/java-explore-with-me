package ru.practicum.ewm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsClient {

    private final WebClient webClient;

    public void saveHit(EndpointHit endpointHit) {
        webClient.post()
                .uri("/hits")
                .bodyValue(endpointHit)
                .retrieve()
                .bodyToMono(EndpointHit.class)
                .block();
    }

    public List<ViewStats> getHit(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        String request = String.format("/stats?start=%s&end=%s&uris=%s&unique=%s",
                encode(String.valueOf(start)),
                encode(String.valueOf(end)),
                uris,
                unique
        );
        return webClient.get()
                .uri(request)
                .retrieve()
                .bodyToFlux(ViewStats.class)
                .collectList()
                .block();
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}