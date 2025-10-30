package com.metamapa.Controller;

import com.metamapa.Domain.dto.EstadisticasDTO;
import com.metamapa.Service.ServiceEstadistica;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
