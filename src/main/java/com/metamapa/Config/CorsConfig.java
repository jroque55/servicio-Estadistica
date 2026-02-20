package com.metamapa.Config;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public FilterRegistrationBean<CorsFilter> customCorsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 1. Permitir credenciales
        config.setAllowCredentials(true);

        // 2. Orígenes permitidos (Tu frontend en producción y local)
        config.setAllowedOrigins(Arrays.asList(
                "https://front-metamapa-lo3l.vercel.app",
                "https://front-metamapa.vercel.app",
                "http://localhost:3000",
                "https://front-metamapa-dun.vercel.app"
        ));

        // 3. Cabeceras y métodos permitidos (OPTIONS es obligatorio)
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // 4. Aplicar a toda la API
        source.registerCorsConfiguration("/**", config);

        // 5. REGISTRAR EL FILTRO CON MÁXIMA PRIORIDAD
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));

        // ESTA LÍNEA ES LA CLAVE: Lo pone antes que tu @Order(1)
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return bean;
    }
}



