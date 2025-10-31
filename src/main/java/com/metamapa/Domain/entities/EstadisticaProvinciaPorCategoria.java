package com.metamapa.Domain.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstadisticaProvinciaPorCategoria implements InterfaceEstadistica {
    private final String categoria;

    // mapa provincia -> cantidad de hechos
    private final Map<String, Integer> provinciaConteo = new HashMap<>();
    private String resultado = InterfaceEstadistica.RESULTADO;

    public EstadisticaProvinciaPorCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public void actualizarResultado() {
        provinciaConteo.clear();
        resultado = InterfaceEstadistica.RESULTADO;

        // obtener el singleton ClienteAgregador
        ClienteAgregador cliente = ClienteAgregador.getInstance();

        if (cliente == null) {
            // no hay cliente disponible -> no se puede calcular
            return;
        }

        List<String> provincias;
        try {
            provincias = cliente.obtenerEstadisticaAgregador("provincia", categoria);
        } catch (Exception ex) {
            return;
        }

        if (provincias == null || provincias.isEmpty()) {
            return;
        }

        // contar cuántas veces aparece cada provincia
        for (String provinciaRaw : provincias) {
            if (provinciaRaw == null) continue;

            String provincia = provinciaRaw.trim();
            if (provincia.isEmpty()) continue;

            provinciaConteo.put(provincia, provinciaConteo.getOrDefault(provincia, 0) + 1);
        }

        // construir resultado (opcional: mostrar el conteo)
        if (!provinciaConteo.isEmpty()) {
            resultado = "Conteo de provincias para categoría '" + categoria + "': " + provinciaConteo;
        }
    }

    public String getResultado() {
        return resultado;
    }

    public Map<String, Integer> getProvinciaConteo() {
        return provinciaConteo;
    }

    public String getCategoria() {
        return categoria;
    }
}

