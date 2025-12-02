package com.metamapa.Domain.dto.input;

import lombok.Data;

@Data
public class SpamSummaryDTO {
    private Long cantSolicitudes;
    private Long cantSpam;

    public SpamSummaryDTO(Long solicitudes,Long cantSpam) {
        this.cantSolicitudes=solicitudes;
        this.cantSpam=cantSpam;
    }
}
