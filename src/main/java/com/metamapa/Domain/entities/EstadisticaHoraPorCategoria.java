package com.metamapa.Domain.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstadisticaHoraPorCategoria implements InterfaceEstadistica {
    private final String categoria;

    // mapa hora -> cantidad de hechos
    private final Map<String, Integer> horaConteo = new HashMap<>();
    private String resultado = InterfaceEstadistica.RESULTADO;

    public EstadisticaHoraPorCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public void actualizarResultado() {
        horaConteo.clear();
        resultado = InterfaceEstadistica.RESULTADO;

        // obtener el singleton ClienteAgregador
        ClienteAgregador cliente = ClienteAgregador.getInstance();

        if (cliente == null) {
            // no hay cliente disponible -> no se puede calcular
            return;
        }

        List<String> horas;
        try {
            horas = cliente.obtenerEstadisticaAgregador("Hora", categoria);
        } catch (Exception ex) {
            return;
        }

        if (horas == null || horas.isEmpty()) {
            return;
        }

        // contar cuantas veces aparece cada hora
        for (String horaRaw : horas) {
            if (horaRaw == null) continue;

            String hora = horaRaw.trim();
            if (hora.isEmpty()) continue;

            horaConteo.put(hora, horaConteo.getOrDefault(hora, 0) + 1); //sumo al map (lo agrega antes si no esta)
        }

        // construir resultado (opcional: mostrar el conteo)
        if (!horaConteo.isEmpty()) {
            resultado = "Conteo de horas para categoría '" + categoria + "': " + horaConteo;
        }
    }

    public String getResultado() {
        return resultado;
    }

    public Map<String, Integer> getHoraConteo() {
        return horaConteo;
    }

    public String getCategoria() {
        return categoria;
    }
}
