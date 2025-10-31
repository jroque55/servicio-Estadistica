package com.metamapa.Controller;

import com.metamapa.Domain.dto.EstadisticasDTO;
import com.metamapa.Service.ServiceEstadistica;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/estadisticas")
public class ControllerEstadistica {

    private final ServiceEstadistica serviceEstadistica;

    public ControllerEstadistica(ServiceEstadistica service) {
        this.serviceEstadistica = service;
    }

    @GetMapping("/ver")
    public ResponseEntity<EstadisticasDTO> verEstadisticas(){
        EstadisticasDTO estadisticas = serviceEstadistica.obtenerEstadisticas();
        return ResponseEntity.status(200).body(estadisticas);
    }

    @GetMapping(value="/exportar", produces = "text/csv")
    public ResponseEntity<String> exportarCSV(){
        String csv = serviceEstadistica.generarCSV();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=estadisticas.csv")
                .body(csv);
    }

    // Endpoint 1: Provincia con más hechos de una colección
    @GetMapping("/coleccion/{idColeccion}/provincia-maxima")
    public ResponseEntity<EstadisticasDTO.ProvinciaMaxima> obtenerProvinciaMaximaDeColeccion(
            @PathVariable Long idColeccion) {
        EstadisticasDTO stats = serviceEstadistica.obtenerEstadisticas();
        EstadisticasDTO.ProvinciaMaxima provincia = stats.getProvinciaMaximaPorColeccion() != null
            ? stats.getProvinciaMaximaPorColeccion().get(idColeccion)
            : null;
        return provincia != null
            ? ResponseEntity.ok(provincia)
            : ResponseEntity.notFound().build();
    }

    // Endpoint 2: Categoría con mayor cantidad de hechos
    @GetMapping("/categoria-maxima")
    public ResponseEntity<EstadisticasDTO.CategoriaMaxima> obtenerCategoriaMaxima() {
        EstadisticasDTO stats = serviceEstadistica.obtenerEstadisticas();
        return stats.getCategoriaMaxima() != null
            ? ResponseEntity.ok(stats.getCategoriaMaxima())
            : ResponseEntity.notFound().build();
    }

    // Endpoint 3: Provincia con más hechos de una categoría
    @GetMapping("/categoria/{categoria}/provincia-maxima")
    public ResponseEntity<EstadisticasDTO.ProvinciaMaxima> obtenerProvinciaMaximaPorCategoria(
            @PathVariable String categoria) {
        EstadisticasDTO stats = serviceEstadistica.obtenerEstadisticas();
        EstadisticasDTO.ProvinciaMaxima provincia = stats.getProvinciaMaximaPorCategoria() != null
            ? stats.getProvinciaMaximaPorCategoria().get(categoria)
            : null;
        return provincia != null
            ? ResponseEntity.ok(provincia)
            : ResponseEntity.notFound().build();
    }

    // Endpoint 4: Hora con más hechos de una categoría
    @GetMapping("/categoria/{categoria}/hora-maxima")
    public ResponseEntity<EstadisticasDTO.HoraMaxima> obtenerHoraMaximaPorCategoria(
            @PathVariable String categoria) {
        EstadisticasDTO stats = serviceEstadistica.obtenerEstadisticas();
        EstadisticasDTO.HoraMaxima hora = stats.getHoraMaximaPorCategoria() != null
            ? stats.getHoraMaximaPorCategoria().get(categoria)
            : null;
        return hora != null
            ? ResponseEntity.ok(hora)
            : ResponseEntity.notFound().build();
    }

    // Endpoint 5: Estadísticas de spam
    @GetMapping("/spam")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasSpam() {
        EstadisticasDTO stats = serviceEstadistica.obtenerEstadisticas();
        Map<String, Object> spamStats = Map.of(
            "totalSolicitudes", stats.getTotalSolicitudesEliminacion() != null ? stats.getTotalSolicitudesEliminacion() : 0,
            "solicitudesSpam", stats.getSolicitudesSpam() != null ? stats.getSolicitudesSpam() : 0,
            "solicitudesNoSpam", stats.getSolicitudesNoSpam() != null ? stats.getSolicitudesNoSpam() : 0,
            "porcentajeSpam", stats.getPorcentajeSpam() != null ? stats.getPorcentajeSpam() : 0.0
        );
        return ResponseEntity.ok(spamStats);
    }

    // Endpoint para todas las categorías con su conteo
    @GetMapping("/categorias")
    public ResponseEntity<Map<String, Integer>> obtenerTodasLasCategorias() {
        EstadisticasDTO stats = serviceEstadistica.obtenerEstadisticas();
        return ResponseEntity.ok(stats.getHechosPorCategoria());
    }

    // Endpoint para forzar actualización manual
    @PostMapping("/actualizar")
    public ResponseEntity<String> actualizarEstadisticas() {
        serviceEstadistica.actualizarUltimasEstadisticas();
        return ResponseEntity.ok("Estadísticas actualizadas correctamente");
    }
}
