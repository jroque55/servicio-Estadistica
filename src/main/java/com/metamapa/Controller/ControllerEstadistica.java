package com.metamapa.Controller;

import com.metamapa.Domain.dto.output.EstadisticaOutputDTO;
import com.metamapa.Domain.entities.InterfaceEstadistica;
import com.metamapa.Service.ServiceEstadistica;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estadisticas")
@CrossOrigin(origins = "http://localhost:3000")
public class ControllerEstadistica {
    //private List<InterfaceEstadistica> estadisticas;
    private final ServiceEstadistica serviceEstadistica;

    public ControllerEstadistica(ServiceEstadistica service) {
        this.serviceEstadistica = service;
    }

    //Que pueda recibir una especie de filtro y envie según corresponda
    @Operation(summary = "Obtiene todas las estadísticas disponibles")
    @ApiResponse(
            responseCode = "200",
            description = "Estadísticas obtenidas correctamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EstadisticaOutputDTO.class))
    )
    @ApiResponse(
            responseCode = "204",
            description = "No se encontró ninguna estadística."
    )
    @GetMapping
    @CrossOrigin(origins= "http://localhost:3000")
    public ResponseEntity<List<EstadisticaOutputDTO>> obtenerEstadisticas(){
        List<EstadisticaOutputDTO> estadisticasDTO = serviceEstadistica.obtenerResultadosDeEstadisticas();
        System.out.println("estadistica: " + estadisticasDTO);
        if (estadisticasDTO == null) {
            return ResponseEntity.noContent().build(); // Código 204
        }
        return ResponseEntity.ok(estadisticasDTO); // Código 200
    }

    @Operation(summary = "Obtiene una estadística específica por su ID")
    @ApiResponse(
            responseCode = "200",
            description = "Estadística encontrada correctamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EstadisticaOutputDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "El ID de estadística proporcionado no fue encontrado."
    )
    @GetMapping("/{id_estadistica}")
    @CrossOrigin(origins= "http://localhost:3000")
    public ResponseEntity<EstadisticaOutputDTO> obtenerEstadisticaPorID(@PathVariable String id_estadistica){
        EstadisticaOutputDTO resultado = serviceEstadistica.obtenerResultadoPorID(id_estadistica);
        if (resultado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultado);
    }

    @Operation(summary = "Exporta todas las estadísticas a un archivo CSV")
    @ApiResponse(
            responseCode = "200",
            description = "Archivo CSV generado y descargado correctamente",
            content = @Content(mediaType = "text/csv") // Tipo de contenido CSV
    )
    @ApiResponse(
            responseCode = "404",
            description = "No hay datos para exportar (lista de estadísticas vacía)."
    )
    @GetMapping(value="/exportar", produces = "text/csv")
    @CrossOrigin(origins= "http://localhost:3000")
    public ResponseEntity<String> exportarCSV(){

        String csv = serviceEstadistica.generarCSV();


        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=estadisticas.csv")
                .body(csv);
    }

}
