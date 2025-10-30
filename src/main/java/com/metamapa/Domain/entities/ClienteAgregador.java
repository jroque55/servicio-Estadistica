package com.metamapa.Domain.entities;

import com.metamapa.Domain.dto.EstadisticasDTO;
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
        this.webClient = builder.baseUrl("http://localhost:8080/api").build();
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
    public List<String> obtenerEstadisticaAgregador(String campo, String categoria) {
        return webClient.get()
                .uri(uriBuilder -> {
                    var b = uriBuilder.path("/Hecho").queryParam("campo", campo);
                    if (categoria != null && !categoria.isEmpty()) b = b.queryParam("categoria", categoria);
                    return b.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
    }

    //5 REVISAR COMO ES QUE SE SABE Q ES SPAM //ARREGLAR lo del block
    public List<String> obtenerDatosSolicitudesSpam() {
        return webClient.get().uri("/solicitudesSpam")
                .retrieve().bodyToMono(EstadisticasDTO.class).block();
    }
}
