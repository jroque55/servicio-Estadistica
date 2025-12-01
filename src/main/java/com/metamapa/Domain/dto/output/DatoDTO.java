package com.metamapa.Domain.dto.output;

import lombok.Data;

@Data
public class DatoDTO {
    private String nombre; // 13:00
    private Long cantidad;

    public DatoDTO() {
    }
    public DatoDTO(String nombre, Long cantidad) {
        this.nombre = nombre;
        this.cantidad = cantidad;
    }
}
