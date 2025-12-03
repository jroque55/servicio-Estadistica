package com.metamapa.Domain.entities;

import com.metamapa.Domain.dto.output.EstadisticaOutputDTO;

public interface IExportador {
    //public String obtenerArchivoTipo(EstadisticasDTO estadisticas);
    String exportar(InterfaceEstadistica estaditica);
}
