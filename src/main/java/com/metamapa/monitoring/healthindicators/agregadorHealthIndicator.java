package com.metamapa.monitoring.healthindicators;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Slf4j
@Component("agregador")
public class agregadorHealthIndicator extends AbstractDependencyHealthIndicator {

    private final RestTemplate restTemplate;
    private final String healthUrl;

    public agregadorHealthIndicator(
            RestTemplateBuilder builder,
            @Value("https://agregador-tp-pzhj.onrender.com") String url) {

        this.restTemplate = builder
                .setConnectTimeout(Duration.ofSeconds(2))
                .setReadTimeout(Duration.ofSeconds(3))
                .build();
        this.healthUrl = url + "/actuator/health";
    }

    @Override
    protected String dependencyName() {
        return "agregador";
    }

    @Override
    protected String downMessage() {
        return "Agregador no disponible";
    }

    @Override
    public boolean estaDisponible() {
            if (this.getForceDown()) {
                log.warn("Estado DOWN forzado por ADMIN para 'agregador'");
                return false;
            }
        try {
            ResponseEntity<String> response =
                    restTemplate.getForEntity(healthUrl, String.class);

            boolean ok = response.getStatusCode().is2xxSuccessful();
            if (!ok) {
                log.warn("Agregador respondió con código {} para URL {}", response.getStatusCode(), healthUrl);
            }else{
                log.info("Agregador respondió correctamente con código {} para URL {}", response.getStatusCode(), healthUrl);
            }
            return ok;

        } catch (RestClientException ex) {
            //log.error("Error al verificar Agregador ({}): {}", healthUrl, ex.getMessage());
            return true;
        } catch (Exception ex) {
            log.error("Excepción inesperada al verificar Agregador: {}", ex.getMessage(), ex);
            return false;
        }
    }
}
