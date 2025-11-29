package com.metamapa.Controller;

import com.metamapa.Domain.dto.EstadisticasDTO;
import com.metamapa.Service.ServiceEstadistica;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estadisticas")
public class ControllerEstadistica {

    private final ServiceEstadistica serviceEstadistica;

    public ControllerEstadistica(ServiceEstadistica service) {
        this.serviceEstadistica = service;
    }

    //Que pueda recibir una especie de filtro y envie según corresponda
    @GetMapping("")
    public ResponseEntity<EstadisticasDTO> verEstadisticas(
            @RequestParam ("id_estadistica") Long id_estadistica
    ){
        EstadisticasDTO estadistica = serviceEstadistica.obtenerEstadistica(id_estadistica);
        return ResponseEntity.status(200).body(estadistica);
    }

    @GetMapping(value="/exportar", produces = "text/csv")
    public ResponseEntity<String> exportarCSV(){
        String csv = serviceEstadistica.generarCSV();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=estadisticas.csv")
                .body(csv);
    }
}
