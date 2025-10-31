package com.metamapa.Domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HechoDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private LocalDateTime fechaHora; // Fecha y hora del hecho
    private LocalDate fecha; // Solo fecha (para compatibilidad)
    private LocalDate fechaDeCarga;
    private UbicacionDTO ubicacion;
    private String etiqueta;

    // Método helper para obtener la hora del día
    public Integer getHora() {
        if (fechaHora != null) {
            return fechaHora.getHour();
        }
        return null;
    }
}

