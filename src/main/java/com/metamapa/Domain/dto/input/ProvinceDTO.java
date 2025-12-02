package com.metamapa.Domain.dto.input;

import lombok.Data;

@Data
public class ProvinceDTO {
    private String provincia;
    private Long cantidad;

    public ProvinceDTO(String provincia,Long cantidad) {

        this.provincia=provincia;
        this.cantidad = cantidad;
    }
}
