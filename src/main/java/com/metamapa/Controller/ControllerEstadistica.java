package com.metamapa.Controller;

import com.metamapa.Domain.dto.output.EstadisticaOutputDTO;
import com.metamapa.Domain.entities.InterfaceEstadistica;
import com.metamapa.Service.ServiceEstadistica;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/estadisticas")
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
    public ResponseEntity<List<EstadisticaOutputDTO>> obtenerEstadisticas(){
        log.info("Obteniendo todas las estadísticas disponibles");
        List<EstadisticaOutputDTO> estadisticasDTO = serviceEstadistica.obtenerResultadosDeEstadisticas();
        System.out.println("estadistica: " + estadisticasDTO);
        if (estadisticasDTO == null || estadisticasDTO.isEmpty()) {
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
    public ResponseEntity<EstadisticaOutputDTO> obtenerEstadisticaPorID(@NotBlank @PathVariable String id_estadistica){
        EstadisticaOutputDTO resultado = serviceEstadistica.obtenerResultadoPorID(id_estadistica);
        if (resultado == null || resultado.getDatos().isEmpty()) {
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
    public ResponseEntity<String> exportarCSV(){

        log.info("Iniciando exportado de estadísticas a CSV");
        String csv = serviceEstadistica.generarCSV();


        log.debug("CSV generado, longitud total: {} caracteres", csv.length());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=estadisticas.csv")
                .body(csv);
    }

}
