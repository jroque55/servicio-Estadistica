package com.metamapa.Config;

import com.metamapa.Service.ServiceEstadistica;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartupLoader {

    private  ServiceEstadistica service;

    public StartupLoader(ServiceEstadistica service) {
        this.service = service;
    }
    @PostConstruct
    public void init() {
        service.cargarEstadisticasDesdeDB();
    }
}
