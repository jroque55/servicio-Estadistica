package com.metamapa.Domain.dto.input;

import lombok.Data;

@Data
public class ProvCatDTO {
    private String provincia;
    private Long cantidad;


    public  ProvCatDTO(String provincia,Long cantidad){
        this.provincia=provincia;
        this.cantidad=cantidad;
    }
}
