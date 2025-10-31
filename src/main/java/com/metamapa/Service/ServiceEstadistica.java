package com.metamapa.Service;

import com.metamapa.Domain.dto.EstadisticasDTO;
import com.metamapa.Domain.entities.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ServiceEstadistica {

    private final ClienteAgregador clienteAgregador;
    private EstadisticasDTO ultimasEstadisticas;

    // Clases de estadísticas individuales
    private final EstadisticaCategoriaMaxima estadisticaCategorias;
    private final EstadisticaSpamEliminacion estadisticaSpam;

    public ServiceEstadistica(ClienteAgregador cliente){
        this.clienteAgregador = cliente;
        this.estadisticaCategorias = new EstadisticaCategoriaMaxima();
        this.estadisticaSpam = new EstadisticaSpamEliminacion();
    }

    @Cacheable("estadisticas")
    public EstadisticasDTO obtenerEstadisticas() {
        if(ultimasEstadisticas == null){
            actualizarUltimasEstadisticas();
        }
        return ultimasEstadisticas;
    }

    public String generarCSV() {
        EstadisticasDTO dto = obtenerEstadisticas();
        ExportadorCSV exp = new ExportadorCSV();
        return exp.obtenerArchivoTipo(dto);
    }

    @Scheduled(fixedRate = 300000) // cada 5 minutos
    @CacheEvict(value = "estadisticas", allEntries = true)
    public void actualizarUltimasEstadisticas(){
        this.ultimasEstadisticas = calcularEstadisticas();
    }

    private EstadisticasDTO calcularEstadisticas() {
        try {
            EstadisticasDTO.EstadisticasDTOBuilder builder = EstadisticasDTO.builder();

            // Metadatos
            builder.fechaActualizacion(LocalDateTime.now());

            // 1. Hechos por provincia de colección - por ahora vacío
            builder.hechosPorProvinciaDeColeccion(new HashMap<>());
            builder.provinciaMaximaPorColeccion(new HashMap<>());

            // 2. Categoría con mayor cantidad de hechos - USANDO CLASE ESPECÍFICA
            estadisticaCategorias.actualizarResultado();
            Map<String, Integer> hechosPorCategoria = estadisticaCategorias.getMapaCategorias();
            builder.hechosPorCategoria(hechosPorCategoria);

            // Construir CategoriaMaxima desde la clase específica
            estadisticaCategorias.getCategoriaConMasHechos().ifPresent(entry -> {
                builder.categoriaMaxima(EstadisticasDTO.CategoriaMaxima.builder()
                        .categoria(entry.getKey())
                        .cantidad(entry.getValue())
                        .build());
            });

            // 3. Provincia por categoría - USANDO CLASES ESPECÍFICAS
            Map<String, Map<String, Integer>> hechosPorProvinciaPorCategoria = new HashMap<>();
            Map<String, EstadisticasDTO.ProvinciaMaxima> provinciaMaximaPorCategoria = new HashMap<>();

            for (String categoria : hechosPorCategoria.keySet()) {
                EstadisticaProvinciaPorCategoria estadisticaProvincia =
                    new EstadisticaProvinciaPorCategoria(categoria);
                estadisticaProvincia.actualizarResultado();

                Map<String, Integer> conteoProvincias = estadisticaProvincia.getProvinciaConteo();
                hechosPorProvinciaPorCategoria.put(categoria, conteoProvincias);

                // Calcular provincia máxima para esta categoría
                if (!conteoProvincias.isEmpty()) {
                    Map.Entry<String, Integer> max = conteoProvincias.entrySet().stream()
                            .max(Map.Entry.comparingByValue())
                            .orElse(null);
                    if (max != null) {
                        provinciaMaximaPorCategoria.put(categoria, EstadisticasDTO.ProvinciaMaxima.builder()
                                .provincia(max.getKey())
                                .cantidad(max.getValue())
                                .build());
                    }
                }
            }

            builder.hechosPorProvinciaPorCategoria(hechosPorProvinciaPorCategoria);
            builder.provinciaMaximaPorCategoria(provinciaMaximaPorCategoria);

            // 4. Hora por categoría - USANDO CLASES ESPECÍFICAS
            Map<String, Map<Integer, Integer>> hechosPorHoraPorCategoria = new HashMap<>();
            Map<String, EstadisticasDTO.HoraMaxima> horaMaximaPorCategoria = new HashMap<>();

            for (String categoria : hechosPorCategoria.keySet()) {
                EstadisticaHoraPorCategoria estadisticaHora =
                    new EstadisticaHoraPorCategoria(categoria);
                estadisticaHora.actualizarResultado();

                Map<String, Integer> horaConteoStr = estadisticaHora.getHoraConteo();
                Map<Integer, Integer> conteoHoras = convertirHorasAInteger(horaConteoStr);
                hechosPorHoraPorCategoria.put(categoria, conteoHoras);

                // Calcular hora máxima para esta categoría
                if (!conteoHoras.isEmpty()) {
                    Map.Entry<Integer, Integer> max = conteoHoras.entrySet().stream()
                            .max(Map.Entry.comparingByValue())
                            .orElse(null);
                    if (max != null) {
                        horaMaximaPorCategoria.put(categoria, EstadisticasDTO.HoraMaxima.builder()
                                .hora(max.getKey())
                                .cantidad(max.getValue())
                                .build());
                    }
                }
            }

            builder.hechosPorHoraPorCategoria(hechosPorHoraPorCategoria);
            builder.horaMaximaPorCategoria(horaMaximaPorCategoria);

            // 5. Solicitudes spam - USANDO CLASE ESPECÍFICA
            estadisticaSpam.actualizarResultado();
            Map<String, Integer> mapaSpam = estadisticaSpam.getMapaSpam();

            // Calcular totales
            int totalSpam = mapaSpam.values().stream().mapToInt(Integer::intValue).sum();
            int totalSolicitudes = totalSpam; // Por ahora solo tenemos spam
            int solicitudesNoSpam = 0;
            double porcentajeSpam = totalSolicitudes > 0 ? 100.0 : 0.0;

            builder.totalSolicitudesEliminacion(totalSolicitudes);
            builder.solicitudesSpam(totalSpam);
            builder.solicitudesNoSpam(solicitudesNoSpam);
            builder.porcentajeSpam(porcentajeSpam);

            return builder.build();

        } catch (Exception e) {
            // En caso de error, retornar estadísticas vacías
            return EstadisticasDTO.builder()
                    .fechaActualizacion(LocalDateTime.now())
                    .hechosPorCategoria(new HashMap<>())
                    .hechosPorProvinciaPorCategoria(new HashMap<>())
                    .hechosPorHoraPorCategoria(new HashMap<>())
                    .provinciaMaximaPorCategoria(new HashMap<>())
                    .horaMaximaPorCategoria(new HashMap<>())
                    .totalSolicitudesEliminacion(0)
                    .solicitudesSpam(0)
                    .solicitudesNoSpam(0)
                    .porcentajeSpam(0.0)
                    .build();
        }
    }

    /**
     * Convierte un mapa de horas en String a Integer
     */
    private Map<Integer, Integer> convertirHorasAInteger(Map<String, Integer> horaConteoStr) {
        Map<Integer, Integer> resultado = new HashMap<>();
        for (Map.Entry<String, Integer> entry : horaConteoStr.entrySet()) {
            try {
                Integer hora = Integer.parseInt(entry.getKey().trim());
                resultado.put(hora, entry.getValue());
            } catch (NumberFormatException e) {
                // Ignorar valores no numéricos
            }
        }
        return resultado;
    }
}
