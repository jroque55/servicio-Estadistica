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
        if (estadistica.getCategorias() != null) {
            estadistica.getCategorias().forEach(estadisticaCategoria -> {
                this.datos.add(new DatoDTO(estadisticaCategoria.getCategoria(),
                        estadisticaCategoria.getCantidad()));
            });
        } else this.datos = new ArrayList<>();
        if(estadistica.getResultado() != null){
            this.resultado = new DatoDTO(estadistica.getResultado().getCategoria(), estadistica.getResultado().getCantidad());
        } else this.resultado = new DatoDTO("Sin Resultado", 0L);
        this.discriminante = new DiscriminanteDTO(estadistica.getDiscriminante());
    }

    public EstadisticaOutputDTO(EstadisticaHoraPorCategoria estadistica) {
        if(estadistica.getCantidadXHoras() != null){
            estadistica.getCantidadXHoras().forEach(estadisticaCategoria -> {
                this.datos.add(new DatoDTO(estadisticaCategoria.getHora()>=10?
                        estadisticaCategoria.getHora().toString()+":00" :
                        "0"+ estadisticaCategoria.getHora().toString()+":00",
                        estadisticaCategoria.getCantidad()));
            });
        }else this.datos = new ArrayList<>();
        if(estadistica.getResultado() != null){
            this.resultado = new DatoDTO(estadistica.getResultado().getHora()>=10?
                    estadistica.getResultado().getHora().toString()+":00" :
                    "0"+ estadistica.getResultado().getHora().toString()+":00"
                    , estadistica.getResultado().getCantidad());
        }else new DatoDTO("Sin Resultado", 0L);

        this.discriminante = new DiscriminanteDTO(estadistica.getDiscriminante());
    }

    public EstadisticaOutputDTO(EstadisticaMaxHechosPorProvinciaDeUnaColeccion estadistica) {
        if(estadistica.getProvincias()!=null) {
            estadistica.getProvincias().forEach(estadisticaCategoria -> {
                this.datos.add(new DatoDTO(estadisticaCategoria.getProvincia(),
                        estadisticaCategoria.getCantidad()));
            });
        }else this.datos = new ArrayList<>();
        if(estadistica.getResultado() != null){
            this.resultado = new DatoDTO(estadistica.getResultado().getProvincia(), estadistica.getResultado().getCantidad());
        }else this.resultado = new DatoDTO("Sin Resultado", 0L);

        this.discriminante = new DiscriminanteDTO(estadistica.getDiscriminante());
    }

    public EstadisticaOutputDTO(EstadisticaProvinciaPorCategoria estadistica) {
        if(estadistica.getProvincias()!=null) {
            estadistica.getProvincias().forEach(estadisticaCategoria -> {
                this.datos.add(new DatoDTO(estadisticaCategoria.getProvincia(),
                        estadisticaCategoria.getCantidad()));
            });
        }else this.datos = new ArrayList<>();
        if(estadistica.getResultado() != null){
            this.resultado = new DatoDTO(estadistica.getResultado().getProvincia(), estadistica.getResultado().getCantidad());
        }else this.resultado = new DatoDTO("Sin Resultado", 0L);
        this.discriminante = new DiscriminanteDTO(estadistica.getDiscriminante());
    }

    public EstadisticaOutputDTO(EstadisticaSpamEliminacion estadistica) {
        if(estadistica.getResultado() != null && estadistica.getCantidadTotal() != null){
            DatoDTO resultado = new DatoDTO("cantidad spam", estadistica.getResultado());
            this.datos.add(new DatoDTO("cantidad total", estadistica.getCantidadTotal()));
            this.datos.add(resultado);
            this.resultado = resultado;
        }else {
            this.datos = new ArrayList<>();
            this.resultado = new DatoDTO("Sin Resultado", 0L);
            }
        this.discriminante = new DiscriminanteDTO(estadistica.getDiscriminante());
    }
}


