package com.metamapa.Domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadisticasDTO {

    // Metadatos
    private LocalDateTime fechaActualizacion;

    // 1. De una colección, ¿en qué provincia se agrupan la mayor cantidad de hechos reportados?
    private Map<Long, Map<String, Integer>> hechosPorProvinciaDeColeccion; // idColeccion -> (provincia -> cantidad)
    private Map<Long, ProvinciaMaxima> provinciaMaximaPorColeccion; // idColeccion -> provincia con más hechos

    // 2. ¿Cuál es la categoría con mayor cantidad de hechos reportados?
    private Map<String, Integer> hechosPorCategoria; // categoría -> cantidad
    private CategoriaMaxima categoriaMaxima; // categoría con más hechos

    // 3. ¿En qué provincia se presenta la mayor cantidad de hechos de una cierta categoría?
    private Map<String, Map<String, Integer>> hechosPorProvinciaPorCategoria; // categoría -> (provincia -> cantidad)
    private Map<String, ProvinciaMaxima> provinciaMaximaPorCategoria; // categoría -> provincia con más hechos

    // 4. ¿A qué hora del día ocurren la mayor cantidad de hechos de una cierta categoría?
    private Map<String, Map<Integer, Integer>> hechosPorHoraPorCategoria; // categoría -> (hora -> cantidad)
    private Map<String, HoraMaxima> horaMaximaPorCategoria; // categoría -> hora con más hechos

    // 5. ¿Cuántas solicitudes de eliminación son spam?
    private Integer totalSolicitudesEliminacion;
    private Integer solicitudesSpam;
    private Integer solicitudesNoSpam;
    private Double porcentajeSpam;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProvinciaMaxima {
        private String provincia;
        private Integer cantidad;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CategoriaMaxima {
        private String categoria;
        private Integer cantidad;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class HoraMaxima {
        private Integer hora;
        private Integer cantidad;
    }
}
