package com.metamapa.Domain.entities;

import com.metamapa.Domain.dto.input.SpamSummaryDTO;
import lombok.Data;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
@Data
@Document(collection = "estadisticas")
@TypeAlias("estadistica_spam")
public class EstadisticaSpamEliminacion extends InterfaceEstadistica {
    private Long resultado;
    private Long cantidadTotal;

    // private final Map<String, Integer> mapaSpam = new HashMap<>();
    private Long totalDeSolicitudes;
    public EstadisticaSpamEliminacion() {
        this.setDiscriminante(new Discriminante(EnumTipoDiscriminante.SIN,"" ));
        this.setTipoEstadistica(EnumTipoEstadistica.CANTSOLICITUDESSPAM);
    }

    @Override
    public void actualizarResultado() {

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
        this.resultado= spam.getCantSpam();
        this.cantidadTotal = spam.getCantSolicitudes();


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
