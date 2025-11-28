package com.metamapa.Domain.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

@Entity
@DiscriminatorValue("MAXCATEGORIACONHECHOS")
public class EstadisticaCategoriaMaxima extends InterfaceEstadistica {

    private final Map<String, Integer> mapaCategorias = new HashMap<>();

    @Override
    public void actualizarResultado() {
        mapaCategorias.clear();
        setResultado(RESULTADO);

        ClienteAgregador cliente = getClienteAgregador();
        if (cliente == null) return;

        List<String> categorias;
        try {
            categorias = cliente.obtenerEstadisticaAgregador();
        } catch (Exception ex) {
            return;
        }

        if (categorias == null || categorias.isEmpty()) return;

        for (String c : categorias) {
            if (c == null) continue;
            String cat = c.trim();
            if (cat.isEmpty()) continue;
            mapaCategorias.merge(cat, 1, Integer::sum);
        }

        if (mapaCategorias.isEmpty()) return;
        Entry<String, Integer> max = Collections.max(mapaCategorias.entrySet(), Comparator.comparingInt(Entry::getValue));
        setResultado(String.format("%s (%d)", max.getKey(), max.getValue()));
    }

    public Map<String, Integer> getMapaCategorias() {
        return Collections.unmodifiableMap(mapaCategorias);
    }

    public Optional<Entry<String, Integer>> getCategoriaConMasHechos() {
        if (mapaCategorias.isEmpty()) return Optional.empty();
        Entry<String, Integer> max = Collections.max(mapaCategorias.entrySet(), Comparator.comparingInt(Entry::getValue));
        return Optional.of(max);
    }

}
