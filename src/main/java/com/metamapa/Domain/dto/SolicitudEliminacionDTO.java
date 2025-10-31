package com.metamapa.Domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudEliminacionDTO {
    private Long id;
    private Long hechoId;
    private LocalDate fecha;
    private String motivo;
    private String estado;
    private boolean esSpam;
}

