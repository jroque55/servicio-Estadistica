package com.metamapa.Domain.entities;

import com.metamapa.Domain.dto.input.ProvinceDTO;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

import java.util.List;

/**
 * Estadística que obtiene de un ClienteAgregador la lista de provincias de una colección
 * y construye un mapa provincia -> cantidad de hechos. Expone el mapa y la provincia con más hechos.
 */
@Data
@Entity
@DiscriminatorValue("MAXPROVINCIADEUNAPROVINCIA")
public class EstadisticaMaxHechosPorProvinciaDeUnaColeccion extends InterfaceEstadistica {
    private final String nombreColeccion;
    private Integer cantidad;

    public EstadisticaMaxHechosPorProvinciaDeUnaColeccion(String nombreColeccion) {
        this.nombreColeccion = nombreColeccion;
    }

    public void actualizarResultado() {
        // obtener el singleton ClienteAgregador
        ClienteAgregador cliente = getClienteAgregador();

        if (cliente == null) {
            // no hay cliente disponible -> no se puede calcular
            return;
        }

        List<ProvinceDTO> provincias;
        try {
            provincias = cliente.obtenerCantHechosXProvinciaDe(this.nombreColeccion);
        } catch (Exception ex) {
            return;
        }
        CalcularResultado(provincias);

    }
        // Mueve la lógica de cálculo de resultado aquí (solicitado entre líneas 47 y 63)
    private void CalcularResultado(List< ProvinceDTO > provincias) {
        if (provincias == null || provincias.isEmpty()) {
            this.setResultado("No hay hechos en la coleccion " + this.nombreColeccion);
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
        this.setResultado(provinciaMax);
        this.setCantidad(maxCantidad);
    }
}
