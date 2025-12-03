package com.metamapa.Domain.dto.output;

import com.metamapa.Domain.entities.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data // Proporciona Getters, Setters, toString, equals y hashCode
@NoArgsConstructor // Genera el constructor sin argumentos (el 'public EstadisticaOutputDTO() {}')
@Schema(description = "DTO de salida que contiene los resultados detallados y el contexto de una estadística.")
public class EstadisticaOutputDTO {

    @Schema(description = "El resultado principal o valor destacado de la estadística (ej: la categoría con más hechos).")
    private DatoDTO resultado;

    @Schema(description = "Lista de datos (valor-cantidad) que componen la estadística (ej: conteo por hora, por provincia).")
    private List<DatoDTO> datos = new ArrayList<>();

    @Schema(description = "Información del discriminante o filtro aplicado a la estadística (ej: ID del mapa o fecha).")
    private DiscriminanteDTO discriminante;
    public EstadisticaOutputDTO(DiscriminanteDTO discriminante, List<DatoDTO> datos, DatoDTO rta) {
        this.discriminante = discriminante;
        this.datos = datos;
        this.resultado = rta;
    }

    //MEJORAR FACTORY
    public EstadisticaOutputDTO(InterfaceEstadistica estadistica) {

    }

    public EstadisticaOutputDTO(EstadisticaCategoriaMaxima estadistica) {
        estadistica.getCategorias().forEach(estadisticaCategoria -> {
            this.datos.add(new DatoDTO(estadisticaCategoria.getCategoria(),
                    estadisticaCategoria.getCantidad()));
        });
        this.resultado = new DatoDTO(estadistica.getResultado().getCategoria(), estadistica.getResultado().getCantidad());
        this.discriminante = new DiscriminanteDTO(estadistica.getDiscriminante());
    }

    public EstadisticaOutputDTO(EstadisticaHoraPorCategoria estadistica) {
        estadistica.getCantidadXHoras().forEach(estadisticaCategoria -> {
            this.datos.add(new DatoDTO(estadisticaCategoria.getHora().toString(),
                    estadisticaCategoria.getCantidad()));
        });
        this.resultado = new DatoDTO(estadistica.getResultado().getHora().toString(), estadistica.getResultado().getCantidad());
        this.discriminante = new DiscriminanteDTO(estadistica.getDiscriminante());
    }

    public EstadisticaOutputDTO(EstadisticaMaxHechosPorProvinciaDeUnaColeccion estadistica) {
        estadistica.getProvincias().forEach(estadisticaCategoria -> {
            this.datos.add(new DatoDTO(estadisticaCategoria.getProvincia(),
                    estadisticaCategoria.getCantidad()));
        });
        this.resultado = new DatoDTO(estadistica.getResultado().getProvincia(), estadistica.getResultado().getCantidad());
        this.discriminante = new DiscriminanteDTO(estadistica.getDiscriminante());
    }

    public EstadisticaOutputDTO(EstadisticaProvinciaPorCategoria estadistica) {
        estadistica.getProvincias().forEach(estadisticaCategoria -> {
            this.datos.add(new DatoDTO(estadisticaCategoria.getProvincia(),
                    estadisticaCategoria.getCantidad()));
        });
        this.resultado = new DatoDTO(estadistica.getResultado().getProvincia(), estadistica.getResultado().getCantidad());
        this.discriminante = new DiscriminanteDTO(estadistica.getDiscriminante());
    }

    public EstadisticaOutputDTO(EstadisticaSpamEliminacion estadistica) {
        DatoDTO resultado = new DatoDTO("cantidad spam", estadistica.getResultado());
        this.datos.add(new DatoDTO("cantidad total", estadistica.getCantidadTotal()));
        this.datos.add(resultado);
        this.resultado = resultado;
        this.discriminante = new DiscriminanteDTO(estadistica.getDiscriminante());
    }
}

