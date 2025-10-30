package com.metamapa.Domain.entities;

import java.util.HashMap;
import java.util.Map;

public class EstadisticaHoraPorCategoria {
    private String categoria;
    private Map<String, Integer> horaConteo= new HashMap<>();

    public EstadisticaHoraPorCategoria(String categoria) {
        this.categoria = categoria;
    }

    public calcularMapa(){
        Map<String, Integer> conteo = new HashMap<>();
        ClienteAgregador cliente = ClienteAgregador.getInstance();
        List<String> horaXHecho = cliente.

    }


}
