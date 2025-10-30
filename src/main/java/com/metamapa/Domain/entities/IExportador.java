package com.metamapa.Domain.entities;

import com.metamapa.Domain.dto.EstadisticasDTO;

public interface IExportador {
    public String obtenerArchivoTipo(EstadisticasDTO estadisticas);
}
