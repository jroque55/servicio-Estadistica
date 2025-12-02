package com.metamapa.Config;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("estadisticas_update")
public class EstadisticaUpdateMarker {

    @Id
    private String id = "singleton";

    private LocalDateTime lastUpdate;
}
