package ar.edu.utnfc.backend.ms_operaciones.clients;

import ar.edu.utnfc.backend.ms_operaciones.clients.dto.GoogleDistanceMatrixResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class GoogleDistanceClient {

    private final WebClient webClient;
    private final String apiKey;

    public GoogleDistanceClient(
            @Value("${google.distance.base-url}") String baseUrl,
            @Value("${google.distance.api-key}") String apiKey) {

        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
        this.apiKey = apiKey;
    }

    public GoogleDistanceMatrixResponse calcularDistancia(
            double origenLat, double origenLng,
            double destinoLat, double destinoLng) {

        String origins = origenLat + "," + origenLng;
        String destinations = destinoLat + "," + destinoLng;

        Mono<GoogleDistanceMatrixResponse> mono = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/maps/api/distancematrix/json")
                        .queryParam("origins", origins)
                        .queryParam("destinations", destinations)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(GoogleDistanceMatrixResponse.class);

        // Para simplificar: bloqueamos (tu MS es síncrono):
        return mono.block();
    }
}
