package com.metamapa.Config;

import com.metamapa.Service.ServiceEstadistica;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EstadisticaScheduler {

    private final ServiceEstadistica service;

    public EstadisticaScheduler(ServiceEstadistica service) {
        this.service = service;
    }

    @Scheduled(fixedDelay = 300_000) // cada 5 minutos
    @SchedulerLock(name = "actualizarResultadosEstadisticas", lockAtMostFor = "10m", lockAtLeastFor = "1m")
    public void actualizarResultadosEstadisticas() {
        service.actualizarResultadosEstadisticas();
    }
}

