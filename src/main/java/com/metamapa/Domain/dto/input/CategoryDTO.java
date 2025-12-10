package com.metamapa.Domain.dto.input;

import lombok.Data;

@Data
public class CategoryDTO {
    private String categoria;
    private Long cantidadHechos;

    public CategoryDTO(String categoria, Long cantidad) {
        this.categoria = categoria;
        this.cantidadHechos = cantidad;
    }
}
