package com.metamapa.Domain.entities;

import com.metamapa.Domain.dto.input.ProvinceDTO;
import lombok.Data;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Estadística que obtiene de un ClienteAgregador la lista de provincias de una colección
 * y construye un mapa provincia -> cantidad de hechos. Expone el mapa y la provincia con más hechos.
 */
@Data
@Document(collection = "estadisticas")
@TypeAlias("estadistica_maxProv")
public class EstadisticaMaxHechosPorProvinciaDeUnaColeccion extends InterfaceEstadistica {
    private Integer cantidad;
    private List<ProvinceDTO> provincias;
    private ProvinceDTO resultado;


    public EstadisticaMaxHechosPorProvinciaDeUnaColeccion(String nombreColeccion) {
        this.setDiscriminante(new Discriminante(EnumTipoDiscriminante.COLECCION,nombreColeccion));
        this.setTipoEstadistica(EnumTipoEstadistica.MAXPROVINCIADEUNACOLECCION);
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
            this.provincias = cliente.obtenerCantHechosXProvinciaDe(this.getDiscriminante().getValor());
        } catch (Exception ex) {
            return;
        }
        CalcularResultado(this.provincias);

    }
        // Mueve la lógica de cálculo de resultado aquí (solicitado entre líneas 47 y 63)
    private void CalcularResultado(List< ProvinceDTO > provincias) {
        if (provincias == null || provincias.isEmpty()) {
            this.setResultado(null);
           // this.setResultado("No hay hechos en la coleccion " + this.getDiscriminante().getValor());
            return;
        }
        int maxCantidad = 0;
        String provinciaMax = null;
        // recorrer y buscar la hora con mayor cantidad
        for (ProvinceDTO provinciaraw : provincias) {
            if (provinciaraw == null) continue;
            Long cant = provinciaraw.getCantidad();
            int cantidadActual = (cant == null) ? 0 : cant.intValue();
            if (cantidadActual > maxCantidad) {
                maxCantidad = cantidadActual;
                provinciaMax = (provinciaraw.getProvincia() == null) ? null : provinciaraw.getProvincia();
            }
        }
        this.setResultado(new ProvinceDTO(provinciaMax,(long)maxCantidad));
        this.setCantidad(maxCantidad);
    }
}
