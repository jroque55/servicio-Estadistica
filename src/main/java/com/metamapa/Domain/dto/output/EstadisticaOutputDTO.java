package com.metamapa.Domain.dto.output;

import com.metamapa.Domain.entities.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class EstadisticaOutputDTO {
    private DatoDTO resultado;
    private List<DatoDTO> datos = new ArrayList<>();
    private DiscriminanteDTO discriminante;

    public EstadisticaOutputDTO() {
    }

    public EstadisticaOutputDTO(DiscriminanteDTO discriminante, List<DatoDTO> datos, DatoDTO rta) {
        this.discriminante = discriminante;
        this.datos = datos;
        this.resultado = rta;
    }

    //MEJORAR FACTORY
    public EstadisticaOutputDTO(InterfaceEstadistica estadistica) {
        switch (estadistica.getTipoEstadistica()){
            case CANTSOLICITUDESSPAM -> new EstadisticaOutputDTO(estadistica);
        }

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

