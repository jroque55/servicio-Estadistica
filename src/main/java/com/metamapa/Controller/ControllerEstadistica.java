package com.metamapa.Controller;

import com.metamapa.Domain.dto.output.EstadisticaOutputDTO;
import com.metamapa.Domain.entities.InterfaceEstadistica;
import com.metamapa.Service.ServiceEstadistica;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/estadisticas")
public class ControllerEstadistica {
    private List<InterfaceEstadistica> estadisticas;
    private final ServiceEstadistica serviceEstadistica;

    public ControllerEstadistica(ServiceEstadistica service) {
        this.serviceEstadistica = service;
    }

    //Que pueda recibir una especie de filtro y envie según corresponda
    @GetMapping
    public ResponseEntity<List<EstadisticaOutputDTO>> verEstadisticas(
            @RequestParam (value = "id_estadistica" , required = false) String id_estadistica
    ){
        //Decir que id_estadistica sea !=0
        List<EstadisticaOutputDTO> estadistica = serviceEstadistica.obtenerResultadosDeEstadisticas(id_estadistica);
        System.out.println("estadistica: " + estadistica);
        if (estadistica == null) {
            return ResponseEntity.status(404).body(null);
        }
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
