package com.metamapa.Domain.entities;
import com.metamapa.Domain.dto.input.CategoryDTO;

import lombok.Data;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Comparator;
import java.util.List;


@Data
@Document(collection = "estadisticas")
@TypeAlias("estadistica_catMax")
public class EstadisticaCategoriaMaxima extends InterfaceEstadistica {
    private List<CategoryDTO> categorias;
    //private Integer cantidad;
    private CategoryDTO resultado;


    public EstadisticaCategoriaMaxima() {
        this.setDiscriminante(new Discriminante(EnumTipoDiscriminante.SIN,"" ));
        this.setTipoEstadistica(EnumTipoEstadistica.MAXCATEGORIACONHECHOS);
    }

    public EstadisticaCategoriaMaxima(String categoria , Long cantidad, List<CategoryDTO> otros) {
        this.setDiscriminante(new Discriminante(EnumTipoDiscriminante.SIN,"" ));
        this.setTipoEstadistica(EnumTipoEstadistica.MAXCATEGORIACONHECHOS);
        this.setResultado(new CategoryDTO(categoria,cantidad));
        this.setCategorias(otros);
    }


    @Override
    public void actualizarResultado() {

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
            this.setResultado(null);
            //this.setResultado("No hay hechos ");
            return;
        }

        categorias.sort(Comparator.comparing(CategoryDTO::getCantidad).reversed());

        this.setResultado(categorias.get(0));

    }

}
