package com.metamapa;

import com.metamapa.Controller.ControllerEstadistica;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EstadisticasApplication {
    public static void main(String[] args) {
    var context = SpringApplication.run(EstadisticasApplication.class, args);
        System.out.println("Servicio de Estadistica INICIADA");

    }
}