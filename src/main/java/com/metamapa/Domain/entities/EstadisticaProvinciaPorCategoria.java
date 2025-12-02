package com.metamapa.Domain.entities;

import com.metamapa.Domain.dto.input.ProvCatDTO;
import lombok.Data;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;
@Data
@Document(collection = "estadisticas")
@TypeAlias("estadistica_maxProvCat")
public class EstadisticaProvinciaPorCategoria extends InterfaceEstadistica {
    private Integer cantidad;
    private List<ProvCatDTO> provincias;
    private ProvCatDTO resultado;

    public EstadisticaProvinciaPorCategoria(String categoria) {

        this.setDiscriminante(new Discriminante(EnumTipoDiscriminante.CATEGORIA,categoria));
        this.setTipoEstadistica(EnumTipoEstadistica.MAXPROVINCIASEGUNCONCATEGORIA);
    }

    @Override
    public void actualizarResultado() {

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
           this.setResultado(null);
            // this.setResultado("No hay hechos con la categoria"+this.getDiscriminante().getValor());
            return;
        }
        calcularResultado();
    }
    public void calcularResultado(){


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
        this.setResultado(new ProvCatDTO(provinciaMaxCategoria,(long)maxCantidad));
        this.setCantidad(maxCantidad);
    }
}