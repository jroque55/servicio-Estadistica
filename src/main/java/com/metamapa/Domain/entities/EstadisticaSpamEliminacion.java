package com.metamapa.Domain.entities;

import com.metamapa.Domain.dto.input.SpamSummaryDTO;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Data;
import org.springframework.data.annotation.TypeAlias;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
@Data
@TypeAlias("estadistica_spam")
public class EstadisticaSpamEliminacion extends InterfaceEstadistica {

    @Transient
   // private final Map<String, Integer> mapaSpam = new HashMap<>();
    private Long totalDeSolicitudes;
    public EstadisticaSpamEliminacion() {
        this.setDiscriminante(new Discriminante(EnumTipoDiscriminante.SIN,"" ));
    }

    public void actualizarEstadistica() {

        ClienteAgregador cliente = getClienteAgregador();
        if (cliente == null) return;

        SpamSummaryDTO spam;
        try {
            // Solicitar lista global para el campo 'spamEliminacion'
            spam= cliente.obtenerDatosSolicitudesSpam();
        } catch (Exception ex) {
            return;
        }

        if (spam == null ) return;

        this.setResultado(spam.getCantSpam().toString());
        this.setTotalDeSolicitudes(spam.getCantSolicitudes());
    }
/*--------------------------------No se si irian---------------------------------------------------
    public Map<String, Integer> getMapaSpam() {
        return Collections.unmodifiableMap(mapaSpam);
    }

    public Optional<Entry<String, Integer>> getElementoConMasSpam() {
        if (mapaSpam.isEmpty()) return Optional.empty();
        Entry<String, Integer> max = Collections.max(mapaSpam.entrySet(), Comparator.comparingInt(Entry::getValue));
        return Optional.of(max);
    }
*/
}
