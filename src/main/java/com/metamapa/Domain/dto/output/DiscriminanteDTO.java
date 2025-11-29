package com.metamapa.Domain.dto.output;

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
}
