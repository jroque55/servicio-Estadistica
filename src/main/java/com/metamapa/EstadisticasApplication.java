package com.metamapa;

import com.metamapa.Controller.ControllerEstadistica;
import com.metamapa.Domain.dto.input.CategoryDTO;
import com.metamapa.Domain.entities.EstadisticaCategoriaMaxima;
import com.metamapa.Domain.entities.InterfaceEstadistica;
import com.metamapa.Domain.entities.repository.IRepositoryEstadisticas;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import java.util.List;

@SpringBootApplication
@EnableScheduling
public class EstadisticasApplication {
    public static void main(String[] args) {
    var context = SpringApplication.run(EstadisticasApplication.class, args);
        System.out.println("Servicio de Estadistica INICIADA");
        IRepositoryEstadisticas repo = context.getBean(IRepositoryEstadisticas.class);

        List<CategoryDTO> categorias = List.of(
                new CategoryDTO("Electrónica", 120L),
                new CategoryDTO("Ropa", 85L),
                new CategoryDTO("Hogar", 42L),
                new CategoryDTO("Deportes", 67L),
                new CategoryDTO("Libros", 33L)
        );

        InterfaceEstadistica est = new EstadisticaCategoriaMaxima("Incendio",200L,categorias); // o la clase que uses
        repo.save(est);

        System.out.println("Guardada OK en Mongo");


    }
}