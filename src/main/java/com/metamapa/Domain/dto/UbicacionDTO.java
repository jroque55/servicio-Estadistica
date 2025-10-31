package com.metamapa.Domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UbicacionDTO {
    private float latitud;
    private float longitud;
    private String provincia;
    private String ciudad;
}

