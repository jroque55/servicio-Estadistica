package com.metamapa.Domain.dto.output;

import com.metamapa.Domain.entities.Discriminante;
import lombok.Data;

@Data
public class DiscriminanteDTO {
    private String valor;
    private String tipo;

    public DiscriminanteDTO() {}

    public DiscriminanteDTO(String valor, String tipo) {
        this.valor = valor;
        this.tipo = tipo;
    }
    public DiscriminanteDTO(Discriminante discriminante) {
        this.valor = discriminante.getValor();
        this.tipo = discriminante.getTipoDiscriminante().name();
    }
}
