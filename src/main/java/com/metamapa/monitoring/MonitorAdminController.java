package com.metamapa.monitoring;

import com.metamapa.monitoring.healthindicators.DatabaseHealthIndicator;
import com.metamapa.monitoring.healthindicators.agregadorHealthIndicator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/monitor")
public class MonitorAdminController {

    private final DatabaseHealthIndicator database;
    private final agregadorHealthIndicator agregador;


    public MonitorAdminController(
            DatabaseHealthIndicator database,
            agregadorHealthIndicator agregador) {

        this.database = database;
        this.agregador = agregador;
    }

    // ---------- FALLAS ----------

    @PostMapping("/fail/database")
    public void failDatabase() {
        database.markDown();
    }

    @PostMapping("/fail/fuente-dinamica")
    public void failFuenteDinamica() {
        agregador.markDown();
    }


    // ---------- RECUPERACIÓN ----------

    @PostMapping("/recover/all")
    public void recoverAll() {
        database.markUp();
        agregador.markUp();
    }
}
