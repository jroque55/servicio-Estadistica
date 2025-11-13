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
@DiscriminatorValue("CANTSOLICITUDESSPAM")
public class EstadisticaSpamEliminacion extends InterfaceEstadistica {

    private final Map<String, Integer> mapaSpam = new HashMap<>();

    public void actualizarResultado() {
        mapaSpam.clear();
        setResultado(RESULTADO);

        ClienteAgregador cliente = getClienteAgregador();
        if (cliente == null) return;

        List<String> items;
        try {
            // Solicitar lista global para el campo 'spamEliminacion'
            items = cliente.obtenerDatosSolicitudesSpam();
        } catch (Exception ex) {
            return;
        }

        if (items == null || items.isEmpty()) return;

        for (String it : items) {
            if (it == null) continue;
            String v = it.trim();
            if (v.isEmpty()) continue;
            mapaSpam.merge(v, 1, Integer::sum);
        }

        if (mapaSpam.isEmpty()) return;

        Entry<String, Integer> max = Collections.max(mapaSpam.entrySet(), Comparator.comparingInt(Entry::getValue));
        setResultado(String.format("%s (%d)", max.getKey(), max.getValue()));
    }

    public Map<String, Integer> getMapaSpam() {
        return Collections.unmodifiableMap(mapaSpam);
    }

    public Optional<Entry<String, Integer>> getElementoConMasSpam() {
        if (mapaSpam.isEmpty()) return Optional.empty();
        Entry<String, Integer> max = Collections.max(mapaSpam.entrySet(), Comparator.comparingInt(Entry::getValue));
        return Optional.of(max);
    }

}
