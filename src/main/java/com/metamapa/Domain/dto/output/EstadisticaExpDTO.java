package com.metamapa.Domain.dto.output;

import com.metamapa.Domain.entities.*;

import java.util.ArrayList;
import java.util.List;

public class EstadisticaExpDTO {

    private Object resultado;
    private List<Object> datos = new ArrayList<>();

    public EstadisticaExpDTO(EstadisticaCategoriaMaxima esta){
        this.resultado=esta.getResultado();
        this.datos.add(esta.getCategorias());
    }

    public EstadisticaExpDTO(EstadisticaHoraPorCategoria esta){
        this.resultado=esta.getResultado();
        this.datos.add(esta.getCantidadXHoras());
    }

    public EstadisticaExpDTO(EstadisticaMaxHechosPorProvinciaDeUnaColeccion esta) {
        this.resultado = esta.getResultado();
        this.datos.add(esta.getProvincias());
    }

    public EstadisticaExpDTO(EstadisticaProvinciaPorCategoria esta) {
        this.resultado = esta.getResultado();
        this.datos.add(esta.getProvincias());
    }

    public EstadisticaExpDTO(EstadisticaSpamEliminacion esta) {
        this.resultado = esta.getResultado();
        this.datos.add(esta.getCantidadTotal());
    }

}
