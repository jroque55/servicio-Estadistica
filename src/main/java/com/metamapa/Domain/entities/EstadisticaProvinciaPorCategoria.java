package com.metamapa.Domain.entities;

import com.metamapa.Domain.dto.input.ProvCatDTO;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
@Entity
@DiscriminatorValue("MAXPROVINCIASEGUNCONCATEGORIA")
public class EstadisticaProvinciaPorCategoria extends InterfaceEstadistica {
    private Integer cantidad;
    private List<ProvCatDTO> provincias;

    public EstadisticaProvinciaPorCategoria(String categoria) {
        this.setDiscriminante(new Discriminante(EnumTipoDiscriminante.CATEGORIA,categoria));
    }

    public void actualizarEstadistica() {

        // obtener el singleton ClienteAgregador
        ClienteAgregador cliente = getClienteAgregador();

        if (cliente == null) {
            // no hay cliente disponible -> no se puede calcular
            return;
        }


        try {
            this.provincias = cliente.obtenerCantHechosPorProvinciaSegun(this.getDiscriminante().getValor());
        } catch (Exception ex) {
            return ;
        }

        if (this.provincias == null || this.provincias.isEmpty()) {
            this.setResultado("No hay hechos con la categoria"+this.getDiscriminante().getValor());
            return;
        }

        int maxCantidad = 0;
        String provinciaMaxCategoria = null;
        //MEJORAR: TODO
       // datos.stream().max()
        for (ProvCatDTO provinciaRaw : this.provincias) {
            if (provinciaRaw == null) continue;

            int cantidadActual = provinciaRaw.getCantidad().intValue();

            if (cantidadActual > maxCantidad) {
                maxCantidad = cantidadActual;
                provinciaMaxCategoria = provinciaRaw.getProvincia();
            }
        }
        this.setResultado(provinciaMaxCategoria);
        this.setCantidad(maxCantidad);

    }
}