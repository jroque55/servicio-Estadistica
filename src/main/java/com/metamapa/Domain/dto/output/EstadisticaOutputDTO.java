package com.metamapa.Domain.dto.output;

import lombok.Data;

import java.util.List;

@Data
public class EstadisticaOutputDTO {
    private DatoDTO resultado;
    private List<DatoDTO> datos;
    private DiscriminanteDTO discriminante;

    public EstadisticaOutputDTO() {}
    public EstadisticaOutputDTO(DiscriminanteDTO discriminante,List<DatoDTO> datos ,DatoDTO rta) {
        this.discriminante = discriminante;
        this.datos = datos;
        this.resultado = rta;
    }
}
