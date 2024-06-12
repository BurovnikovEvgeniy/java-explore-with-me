package ru.practicum.ewm;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

@Service
@RequiredArgsConstructor
public class StatsClient {

    @Autowired
    private final WebClient webClient;

    private final ResourceBundle resource = ResourceBundle.getBundle("messages");

    public EndpointHit saveHit(EndpointHit endpointHit) {
        String uri = resource.getString("client.hits");
        return webClient.post()
                .uri(uri)
                .bodyValue(endpointHit)
                .retrieve()
                .bodyToMono(EndpointHit.class)
                .block();
    }

    public List<ViewStats> getHit(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        String uri = resource.getString("client.stats");
        String request = String.format(uri, encode(String.valueOf(start)), encode(String.valueOf(end)), uris, unique);
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