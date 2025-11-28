package com.metamapa.Domain.entities;

import com.metamapa.Domain.dto.input.ProvCatDTO;
import com.metamapa.Domain.dto.input.SpamSummaryDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class ClienteAgregador {
    private final WebClient webClient;

    // Singleton instance (the Spring-managed bean will set this instance on construction)
    private static volatile ClienteAgregador INSTANCE;

    public ClienteAgregador(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8080/estadisticas").build();
        // set singleton reference
        INSTANCE = this;
    }

    public static ClienteAgregador getInstance() {
        return INSTANCE;
    }
    //1 Campo  <provincia de una coleccion en especifico check

    //VER ESO DEL CAST
    public List<String> obtenerEstadisticaAgregador(long idColeccion, String campo, String categoria) {
                 return webClient.get().uri(uriBuilder -> {
                    var b = uriBuilder.path("/colecciones/{idColeccion}").queryParam("campo", campo);
                    if (categoria != null && !categoria.isEmpty()) b = b.queryParam("categoria", categoria);
                    return b.build(idColeccion);
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
    }
    public List<ProvCatDTO> obtenerEstadisticaAgregador(String categoria) {
        return webClient.get()
                .uri("/categoria-provincia")//ESTO ES DIFERENTE EN EL AGREGADOR PEROES PAR ADIFERENCIA POR AHORA
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ProvCatDTO>>() {})
                .block();
    }

    //5 REVISAR COMO ES QUE SE SABE Q ES SPAM //ARREGLAR lo del block
    public SpamSummaryDTO obtenerDatosSolicitudesSpam() {
        return webClient.get().uri("/solicitudesSpam")
                .retrieve()
                .bodyToMono(SpamSummaryDTO.class) // mapea directo al DTO
                .block();
    }
}
