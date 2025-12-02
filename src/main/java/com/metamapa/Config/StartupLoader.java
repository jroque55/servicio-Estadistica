package com.metamapa.Config;

import com.metamapa.Service.ServiceEstadistica;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class StartupLoader {

    private final ServiceEstadistica service;

    public StartupLoader(ServiceEstadistica service) {
        this.service = service;
    }

    @PostConstruct
    public void init() {
        service.cargarEstadisticasDesdeDB();
    }
}
