package com.metamapa.Domain.entities;

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

    /**
     * Obtiene una columna específica de hechos con filtro opcional por categoría.
     * Este método es ligero porque solo trae los valores de una columna, no los hechos completos.
     *
     * @param campo El nombre de la columna a traer (ej: "provincia", "categoria", "hora")
     * @param categoria Filtro opcional por categoría. Si es null, trae todos los hechos
     * @return Lista de valores de la columna solicitada
     */
    public List<String> obtenerEstadisticaAgregador(String campo, String categoria) {
        return webClient.get()
                .uri(uriBuilder -> {
                    var b = uriBuilder.path("/hechos/columna").queryParam("campo", campo);
                    if (categoria != null && !categoria.isEmpty()) {
                        b = b.queryParam("categoria", categoria);
                    }
                    return b.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
    }

    /**
     * Obtiene una columna específica de hechos de una colección con filtro opcional por categoría.
     *
     * @param idColeccion ID de la colección
     * @param campo El nombre de la columna a traer
     * @param categoria Filtro opcional por categoría
     * @return Lista de valores de la columna solicitada
     */
    public List<String> obtenerEstadisticaAgregador(long idColeccion, String campo, String categoria) {
        return webClient.get().uri(uriBuilder -> {
            var b = uriBuilder.path("/colecciones/{idColeccion}/columna")
                    .queryParam("campo", campo);
            if (categoria != null && !categoria.isEmpty()) {
                b = b.queryParam("categoria", categoria);
            }
            return b.build(idColeccion);
        })
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
        .block();
    }

    /**
     * Obtiene información sobre solicitudes de eliminación que son spam.
     *
     * @return Lista con información de spam (puede ser contadores o IDs)
     */
    public List<String> obtenerDatosSolicitudesSpam() {
        return webClient.get().uri("/solicitudes/spam/info")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
    }
}
