package com.metamapa.Controller;

import com.metamapa.Domain.dto.output.EstadisticaOutputDTO;
import com.metamapa.Domain.entities.InterfaceEstadistica;
import com.metamapa.Service.ServiceEstadistica;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<EstadisticaOutputDTO>> obtenerEstadisticas(){
        List<EstadisticaOutputDTO> estadisticasDTO = serviceEstadistica.obtenerResultadosDeEstadisticas();
        System.out.println("estadistica: " + estadisticasDTO);
        if (estadisticasDTO == null) {
            return ResponseEntity.noContent().build(); // Código 204
        }
        return ResponseEntity.ok(estadisticasDTO); // Código 200
    }
    @GetMapping("/{id_estadistica}")
    public ResponseEntity<EstadisticaOutputDTO> obtenerEstadisticaPorID(@PathVariable String id_estadistica){
        EstadisticaOutputDTO resultado = serviceEstadistica.obtenerResultadoPorID(id_estadistica);
        if (resultado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultado);
    }


    @GetMapping(value="/exportar", produces = "text/csv")
    public ResponseEntity<String> exportarCSV(){

        String csv = serviceEstadistica.generarCSV(this.estadisticas);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=estadisticas.csv")
                .body(csv);
    }

}
