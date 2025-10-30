package com.metamapa.Domain.entities;

import com.metamapa.Domain.dto.EstadisticasDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ClienteAgregador {
    private final WebClient webClient;

    public ClienteAgregador(WebClient.Builder builder){
        this.webClient = builder.baseUrl("http://localhost:8080/api").build();
    }

    public EstadisticasDTO obtenerEstadisticasAgregador(){
       return webClient.get().uri("/estadisticas").retrieve().bodyToMono(EstadisticasDTO.class).block();
    }

}
