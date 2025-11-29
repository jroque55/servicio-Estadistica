package com.metamapa.Domain.entities;

import com.metamapa.Domain.dto.input.CategoryDTO;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

import java.util.List;


@Data
@Entity
@DiscriminatorValue("MAXCATEGORIACONHECHOS")
public class EstadisticaCategoriaMaxima extends InterfaceEstadistica {
    private List<CategoryDTO> categorias;
    private Integer cantidad;


    public EstadisticaCategoriaMaxima() {
        this.setDiscriminante(new Discriminante(EnumTipoDiscriminante.SIN,"" ));
    }


    public void actualizarEstadistica() {

        ClienteAgregador cliente = getClienteAgregador();
        if (cliente == null) return;

        try {
            this.categorias = cliente.obtenerCantHechosPorCategoria();
        } catch (Exception ex) {
            return;
        }
        CalcularResultado(this.categorias);
    }
    // Mueve la lógica de cálculo de resultado aquí (solicitado entre líneas 47 y 63)
    private void CalcularResultado(List<CategoryDTO> categorias) {
        if (categorias == null || categorias.isEmpty()) {
            this.setResultado("No hay hechos ");
            return;
        }

        int maxCantidad = 0;
        String categoriaMax = null;
        // recorrer y buscar la hora con mayor cantidad
        for (CategoryDTO categoriaraw : categorias) {
            if (categoriaraw == null) continue;

            Long cant = categoriaraw.getCantidad();
            int cantidadActual = (cant == null) ? 0 : cant.intValue();

            if (cantidadActual > maxCantidad) {
                maxCantidad = cantidadActual;
                //VER ESTO
                categoriaMax = (categoriaraw.getCategoria() != null) ? categoriaraw.getCategoria() : null ;
            }
        }

        this.setResultado(categoriaMax);
        this.setCantidad(maxCantidad);
    }

}
