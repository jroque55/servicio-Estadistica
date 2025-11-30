package com.metamapa.Domain.entities;

import com.metamapa.Domain.dto.input.ProvinceDTO;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import org.springframework.data.annotation.TypeAlias;

import java.util.List;

/**
 * Estadística que obtiene de un ClienteAgregador la lista de provincias de una colección
 * y construye un mapa provincia -> cantidad de hechos. Expone el mapa y la provincia con más hechos.
 */
@Data
@TypeAlias("estadistica_maxProv")
public class EstadisticaMaxHechosPorProvinciaDeUnaColeccion extends InterfaceEstadistica {
    private Integer cantidad;
    private List<ProvinceDTO> provincias;


    public EstadisticaMaxHechosPorProvinciaDeUnaColeccion(String nombreColeccion) {
        this.setDiscriminante(new Discriminante(EnumTipoDiscriminante.COLECCION,nombreColeccion));
    }

    public void actualizarEstadistica() {
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
            this.setResultado("No hay hechos en la coleccion " + this.getDiscriminante().getValor());
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
