package com.metamapa.Domain;

import com.metamapa.Domain.dto.output.EstadisticaOutputDTO;
import com.metamapa.Domain.entities.EstadisticaCategoriaMaxima;
import com.metamapa.Domain.entities.InterfaceEstadistica;
import org.springframework.stereotype.Component;

@Component
public class FactoryEstadisticaDTO {

    public EstadisticaOutputDTO crearEstadisticaDTO(InterfaceEstadistica estadistica) {
        EstadisticaOutputDTO estadisticaDTO = null;
        switch (estadistica.getTipoEstadistica()) {
            case MAXCATEGORIACONHECHOS -> estadisticaDTO = new EstadisticaOutputDTO((EstadisticaCategoriaMaxima) estadistica);
        }
        return estadisticaDTO;
    }
}
