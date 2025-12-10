package com.metamapa;

import com.metamapa.Config.EstadisticaScheduler;
import com.metamapa.Controller.ControllerEstadistica;
import com.metamapa.Domain.dto.input.CategoryDTO;
import com.metamapa.Domain.dto.output.EstadisticaOutputDTO;
//import com.metamapa.Domain.entities.EstadisticaCategoriaMaxima;
import com.metamapa.Domain.entities.ClienteAgregador;
import com.metamapa.Domain.entities.ExportadorCSV;
import com.metamapa.Domain.entities.InterfaceEstadistica;
import com.metamapa.Domain.entities.repository.IRepositoryEstadisticas;
import com.metamapa.Service.ServiceEstadistica;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
@EnableScheduling
public class EstadisticasApplication {
    public static void main(String[] args) {
    var context = SpringApplication.run(EstadisticasApplication.class, args);
        System.out.println("Servicio de Estadistica INICIADA");
        IRepositoryEstadisticas repo = context.getBean(IRepositoryEstadisticas.class);
       // System.out.println(repo.findAll().size());
       // List<CategoryDTO> categorias = List.of(
       //         new CategoryDTO("Electrónica", 120L),
       //         new CategoryDTO("Ropa", 85L),
       //         new CategoryDTO("Hogar", 42L),
       //         new CategoryDTO("Deportes", 67L),
       //         new CategoryDTO("Libros", 33L)
       // );

        //ClienteAgregador cliente = context.getBean(ClienteAgregador.class);
        ServiceEstadistica service = context.getBean(ServiceEstadistica.class);
        //EstadisticaScheduler scheduler = new EstadisticaScheduler(service);
        service.actualizarEstadisticas();
        service.actualizarResultadosEstadisticas();
       // List<InterfaceEstadistica> estadisticas = service.getEstadisticas();

        /*
        InterfaceEstadistica est = new EstadisticaCategoriaMaxima("Incendio",200L,categorias); // o la clase que uses
        repo.save(est);

        System.out.println("Guardada OK en Mongo");


        Optional<InterfaceEstadistica> estadistica = repo.findById("692edc046a5cfa2bff372537");
        InterfaceEstadistica esta= estadistica.get();
        EstadisticaCategoriaMaxima cat = (EstadisticaCategoriaMaxima) esta;

        System.out.println("Datos Estadisticas : ");
        System.out.println(" " +cat.getTipoEstadistica());
        System.out.println(" " +cat.getResultado());

        EstadisticaOutputDTO estadisticaOutputDTO = new EstadisticaOutputDTO(cat);
        */
        // ExportadorCSV exportador= context.getBean(ExportadorCSV.class);
        //String valorCSV = exportador.exportar(estadisticaOutputDTO);
        //String verEstadisticas = service.generarCSV();
        //System.out.println("Valor Exportable ");
        //System.out.println(" " +verEstadisticas);

        //System.out.println("Mandale Mecha");




    }
}