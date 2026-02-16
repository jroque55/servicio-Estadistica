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
    private final String url;

    public agregadorHealthIndicator(
            RestTemplateBuilder builder,
            @Value("https://agregador-79iw.onrender.com/estadisticas") String url) {

        this.restTemplate = builder
                .setConnectTimeout(Duration.ofSeconds(2))
                .setReadTimeout(Duration.ofSeconds(3))
                .build();
        this.url = url;
    }

    @Override
    protected String dependencyName() {
        return "fuenteDinamica";
    }

    @Override
    protected String downMessage() {
        return "Fuente dinamica no disponible";
    }

    @Override
    public boolean estaDisponible() {
        try {
            ResponseEntity<String> response =
                    restTemplate.getForEntity(url, String.class);

            boolean ok = response.getStatusCode().is2xxSuccessful();
            if (!ok) {
                log.warn("FuenteDinamica respondió con código {} para URL {}", response.getStatusCode(), url);
            }else{
                log.info("FuenteDinamica respondió correctamente con código {} para URL {}", response.getStatusCode(), url);
            }
            return ok;

        } catch (RestClientException ex) {
            log.error("Error al verificar FuenteDinamica ({}): {}", url, ex.getMessage());
            return false;
        } catch (Exception ex) {
            log.error("Excepción inesperada al verificar FuenteDinamica: {}", ex.getMessage(), ex);
            return false;
        }
    }
}
