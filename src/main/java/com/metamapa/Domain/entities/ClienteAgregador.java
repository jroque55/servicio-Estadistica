package com.metamapa.Domain.entities;

import com.metamapa.Domain.dto.input.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
@Slf4j
@Component
public class ClienteAgregador {
    private final WebClient webClient;

    // Singleton instance (the Spring-managed bean will set this instance on construction)
    private static volatile ClienteAgregador INSTANCE;

    public ClienteAgregador(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://agregador-tp-pzhj.onrender.com/estadisticas").build();
        // set singleton reference
        INSTANCE = this;
    }

    public static ClienteAgregador getInstance() {
        return INSTANCE;
    }

    public List<ProvCatDTO> obtenerCantHechosPorProvinciaSegun(String categoria) {
        log.debug("ClienteAgregador: Solicitando cantidad de hechos por provincia para categoría '{}'", categoria);
        return webClient.get()
                .uri("/provinciaxcat?categoria=" + categoria)//ESTO ES DIFERENTE EN EL AGREGADOR PEROES PAR ADIFERENCIA POR AHORA
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ProvCatDTO>>() {})
                .block();
    }
    public List<CatHourDTO> obtenerHechosPorHoraSegun(String categoria) {
        log.debug("ClienteAgregador: Solicitando cantidad de hechos por hora para categoría '{}'", categoria);
        return webClient.get()
                .uri("/hora?categoria=" + categoria)//ESTO ES DIFERENTE EN EL AGREGADOR PEROES PAR ADIFERENCIA POR AHORA
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CatHourDTO>>() {})
                .block();
    }
    public List<ProvinceDTO> obtenerCantHechosXProvinciaDe(String nombreColeccion) {
        log.debug("ClienteAgregador: Solicitando cantidad de hechos por provincia para colección '{}'", nombreColeccion);
        List<ProvinceDTO> lista = webClient.get()
                .uri("/provinciaxcol?coleccion=" + nombreColeccion)//ESTO ES DIFERENTE EN EL AGREGADOR PEROES PAR ADIFERENCIA POR AHORA
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ProvinceDTO>>() {})
                .block();
        return lista;
    }
    public List<CategoryDTO> obtenerCantHechosPorCategoria() {
        log.debug("ClienteAgregador: Solicitando cantidad de hechos por categoría");
        return webClient.get()
                .uri("/categoria")//ESTO ES DIFERENTE EN EL AGREGADOR PEROES PAR ADIFERENCIA POR AHORA
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CategoryDTO>>() {})
                .block();
    }

    //5 REVISAR COMO ES QUE SE SABE Q ES SPAM //ARREGLAR lo del block
    public SpamSummaryDTO obtenerDatosSolicitudesSpam() {
        log.debug("ClienteAgregador: Solicitando datos de solicitudes de eliminación , siendo spam  y total de solicitudes");
        return webClient.get().uri("/solicitudesSpam")
                .retrieve()
                .bodyToMono(SpamSummaryDTO.class) // mapea directo al DTO
                .block();
    }

    public List<String> obtenerColecciones() {
        log.debug("ClienteAgregador: Solicitando nombres de colecciones");
        return webClient.get()
                .uri("/colecciones/nombre")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
    }


    public List<String> obtenerCategorias() {
        log.debug("ClienteAgregador: Solicitando nombres de categorías");
        return webClient.get()
                .uri("/categorias/nombre")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
    }
}
