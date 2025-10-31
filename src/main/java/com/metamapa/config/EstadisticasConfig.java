package com.metamapa.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableCaching
public class EstadisticasConfig {
    // Esta clase habilita el scheduling para actualización periódica
    // y el caching para mejorar el rendimiento
}

