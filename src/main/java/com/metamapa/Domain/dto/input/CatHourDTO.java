package com.metamapa.Domain.dto.input;

import lombok.Data;

@Data
public class CatHourDTO {
    private Integer hora;
    private Long cantidad;

    public CatHourDTO(Integer hora,Long cantidad) {
        this.hora=hora;
        this.cantidad = cantidad;
    }
}
