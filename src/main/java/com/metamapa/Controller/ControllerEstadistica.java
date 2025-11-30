package com.metamapa.Controller;

import com.metamapa.Domain.dto.output.EstadisticaOutputDTO;
import com.metamapa.Domain.entities.InterfaceEstadistica;
import com.metamapa.Service.ServiceEstadistica;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/estadisticas")
public class ControllerEstadistica {
    private List<InterfaceEstadistica> estadisticas;
    private final ServiceEstadistica serviceEstadistica;
    private LocalDateTime ultimaActualizacion;

    public ControllerEstadistica(ServiceEstadistica service) {
        this.serviceEstadistica = service;
    }

    //Que pueda recibir una especie de filtro y envie según corresponda
    @GetMapping("")
    public ResponseEntity<List<EstadisticaOutputDTO>> verEstadisticas(
            @RequestParam (value = "id_estadistica" , required = false) Long id_estadistica
    ){
        //Deberìan ser 5 o 10 min, no sè si ese 5 son 5 min jaja para, la ultima actualizacion deberìa traerse o rtern persistida en una BBDD
        if(ultimaActualizacion.compareTo(LocalDateTime.now()) > 5 ){
            this.serviceEstadistica.actualizarEstadisticas();
        }
        List<EstadisticaOutputDTO> estadistica = serviceEstadistica.obtenerEstadisticas(id_estadistica);
        return ResponseEntity.status(200).body(estadistica);
    }


    @GetMapping(value="/exportar", produces = "text/csv")
    public ResponseEntity<String> exportarCSV(){

        String csv = serviceEstadistica.generarCSV(this.estadisticas);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=estadisticas.csv")
                .body(csv);
    }

}
