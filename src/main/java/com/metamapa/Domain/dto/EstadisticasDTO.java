package com.metamapa.Domain.dto;

import com.metamapa.Domain.entities.InterfaceEstadistica;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstadisticasDTO {

    private List<InterfaceEstadistica> estadisticas;  //<<nombreEstadistica, resultadosEstadistica>>
    public EstadisticasDTO() {}
    public Map<String, String> getEstadisticas() {
        return new HashMap<>();
    }
}
