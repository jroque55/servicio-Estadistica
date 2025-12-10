package com.metamapa.Config;

import com.metamapa.Service.ServiceEstadistica;
import lombok.NoArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
public class EstadisticaScheduler {

    private ServiceEstadistica service;

    public EstadisticaScheduler(ServiceEstadistica service) {
        this.service = service;
    }

    @Scheduled(fixedDelay = 300_000) // cada 5 minutos
    @SchedulerLock(name = "actualizarResultados", lockAtMostFor = "60m", lockAtLeastFor = "10m")
    public void actualizarResultados() {
        service.actualizarResultadosEstadisticas();
    }


    @Scheduled(cron = "0 0 2 * * *") // todos los días 02:00 AM
    @SchedulerLock(name = "actualizarEstadisticasDiaries", lockAtLeastFor = "70m", lockAtMostFor = "10m")
    public void actualizarEstadisticas() {
        service.actualizarEstadisticas();
    }
}

