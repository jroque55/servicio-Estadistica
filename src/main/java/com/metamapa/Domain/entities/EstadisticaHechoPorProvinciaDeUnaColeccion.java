package com.metamapa.Domain.entities;

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
public class EstadisticaHechoPorProvinciaDeUnaColeccion implements InterfaceEstadistica {
    private final Long idColeccion;

    // mapa provincia -> cantidad de hechos
    private final Map<String, Integer> mapaProvincias = new HashMap<>();
    private String resultado = InterfaceEstadistica.RESULTADO;

    public EstadisticaHechoPorProvinciaDeUnaColeccion(Long idColeccion) {
        this.idColeccion = idColeccion;
    }

    @Override
    public void actualizarResultado() {
        mapaProvincias.clear();
        resultado = InterfaceEstadistica.RESULTADO;

        // obtener el singleton ClienteAgregador
        ClienteAgregador cliente = ClienteAgregador.getInstance();

        if (cliente == null) {
            // no hay cliente disponible -> no se puede calcular
            return;
        }

        List<String> provincias;
        try {
            provincias = cliente.obtenerEstadisticaAgregador(idColeccion, "provincia", null);
        } catch (Exception ex) {
            return;
        }

        if (provincias == null || provincias.isEmpty()) {
            return;
        }

        for (String provRaw : provincias) {
            if (provRaw == null) continue;
            String prov = provRaw.trim();
            if (prov.isEmpty()) continue;
            mapaProvincias.merge(prov, 1, Integer::sum);
        }

        if (mapaProvincias.isEmpty()) {
            resultado = InterfaceEstadistica.RESULTADO;
            return;
        }

        Entry<String, Integer> maxEntry = Collections.max(mapaProvincias.entrySet(), Comparator.comparingInt(Entry::getValue));
        resultado = String.format("%s (%d)", maxEntry.getKey(), maxEntry.getValue());
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

    public String getResultado() {
        return resultado;
    }
}
