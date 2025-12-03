package com.metamapa.Domain;

import com.metamapa.Domain.dto.output.EstadisticaOutputDTO;
import com.metamapa.Domain.entities.*;
import com.metamapa.Domain.entities.EstadisticaCategoriaMaxima;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FactoryEstadisticaDTO {

    public EstadisticaOutputDTO crearEstadisticaDTO(InterfaceEstadistica estadistica) {
        EstadisticaOutputDTO estadisticaDTO = null;
        switch (estadistica.getTipoEstadistica()) {
           case MAXCATEGORIACONHECHOS -> estadisticaDTO = new EstadisticaOutputDTO((EstadisticaCategoriaMaxima) estadistica);
            case MAXHORASEGUNCATEGORIA -> estadisticaDTO= new EstadisticaOutputDTO((EstadisticaHoraPorCategoria) estadistica);
            case MAXPROVINCIADEUNACOLECCION -> estadisticaDTO= new EstadisticaOutputDTO((EstadisticaMaxHechosPorProvinciaDeUnaColeccion) estadistica);
            case MAXPROVINCIASEGUNCONCATEGORIA -> estadisticaDTO= new EstadisticaOutputDTO((EstadisticaProvinciaPorCategoria) estadistica);
            case CANTSOLICITUDESSPAM -> estadisticaDTO= new EstadisticaOutputDTO((EstadisticaSpamEliminacion) estadistica

            );
        }
        return estadisticaDTO;
    }
    public List<EstadisticaOutputDTO> crearListaEstadisticaDTO(List<InterfaceEstadistica> estadisticas) {
        List<EstadisticaOutputDTO> estadisticasDTO = new ArrayList<>();
        estadisticas.forEach(estadistica -> {
           estadisticasDTO.add(crearEstadisticaDTO(estadistica));
        });
        return estadisticasDTO;
    }
}
