package com.metamapa.Domain.entities;

import com.metamapa.Domain.dto.input.CatHourDTO;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@DiscriminatorValue("MAXHORASEGUNCATEGORIA")
public class EstadisticaHoraPorCategoria extends InterfaceEstadistica {
    private final String categoria;
    private long cantidad;

    public EstadisticaHoraPorCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void actualizarResultado() {

        // obtener el singleton ClienteAgregador
        ClienteAgregador cliente = getClienteAgregador();

        if (cliente == null) {
            // no hay cliente disponible -> no se puede calcular
            return;
        }

        List<CatHourDTO> cantidadXHoras;
        try {
            cantidadXHoras = cliente.obtenerEstadisticaAgregador();
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
            setResultado("Conteo de horas para categoría '" + categoria + "': " + horaConteo);
        }
    }

    public Map<String, Integer> getHoraConteo() {
        return horaConteo;
    }

    public String getCategoria() {
        return categoria;
    }
}
