package com.metamapa.Domain.entities;

import com.metamapa.Domain.dto.input.ProvCatDTO;
import com.metamapa.Domain.dto.input.ProvinceDTO;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * Estadística que obtiene de un ClienteAgregador la lista de provincias de una colección
 * y construye un mapa provincia -> cantidad de hechos. Expone el mapa y la provincia con más hechos.
 */
@Entity
@DiscriminatorValue("MAXPROVINCIADEUNAPROVINCIA")
public class EstadisticaMaxHechosPorProvinciaDeUnaColeccion extends InterfaceEstadistica {
    private final String coleccion;
    // mapa provincia -> cantidad de hechos
    private final Map<String, Integer> mapaProvincias = new HashMap<>();

    public EstadisticaMaxHechosPorProvinciaDeUnaColeccion(String idColeccion) {
        //Siempre me llega la ID bien??
        this.coleccion = idColeccion;
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
            provincias = cliente.obtenerEstadisticaAgregador(this.coleccion, "provincia", null);
        } catch (Exception ex) {
            return;
        }

        if (provincias == null || provincias.isEmpty()) {
            return;
        }
        //Se puede optimizar haciendola un metodo aparte???
        for (String provRaw : provincias) {
            if (provRaw == null) continue;
            String prov = provRaw.trim();
            if (prov.isEmpty()) continue;
            mapaProvincias.merge(prov, 1, Integer::sum);
        }

        if (mapaProvincias.isEmpty()) return;

        )));
    }

    // devuelve el mapa inmutable
    public Map<String, Integer> getMapaProvincias() {
        return Collections.unmodifiableMap(mapaProvincias);
    }

    // devuelve la provincia con más hechos y su cuenta si existe
    public Optional<Entry<String, Integer>> getProvinciaConMasHechos() {
        if (mapaProvincias.isEmpty()) return Optional.empty();
        Entry<String, Integer> max = Collections.max(mapaProvincias.entrySet(), Comparator.comparingInt(Entry::getValue));
        return Optional.of(max);
    }
}
