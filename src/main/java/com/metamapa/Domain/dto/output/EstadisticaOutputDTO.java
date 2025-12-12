package com.metamapa.Domain.dto.output;

import com.metamapa.Domain.entities.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    private String descripcion;
    public EstadisticaOutputDTO(DiscriminanteDTO discriminante, List<DatoDTO> datos, DatoDTO rta,String descripcion) {
        this.discriminante = discriminante;
        this.datos = datos;
        this.resultado = rta;
        this.descripcion= descripcion;
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
        this.descripcion = "Categoria con mas hechos";
    }

    public EstadisticaOutputDTO(EstadisticaHoraPorCategoria estadistica) {
        if(estadistica.getCantidadXHoras() != null){
            estadistica.getCantidadXHoras().forEach(estadisticaCategoria -> {
                this.datos.add(new DatoDTO(estadisticaCategoria.getHora().toString(),
                        estadisticaCategoria.getCantidad()));
            });
        }else this.datos = new ArrayList<>();
        if(estadistica.getResultado() != null){
            this.resultado = new DatoDTO(estadistica.getResultado().getHora().toString(),
                    estadistica.getResultado().getCantidad());
        }else new DatoDTO("Sin Resultado", 0L);

        this.discriminante = new DiscriminanteDTO(estadistica.getDiscriminante());
        this.descripcion = "Hora con mas hechos reportados con la categoría: ";
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
        this.descripcion = "Provincia con más hechos en la colección: " ;
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
        this.descripcion = "Provincia con más hechos reportados con la categoría: ";
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
        this.descripcion = "Cantidad de solicitudes de eliminación marcadas como spam";
    }
}


