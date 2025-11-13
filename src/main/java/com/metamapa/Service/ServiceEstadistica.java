package com.metamapa.Service;

import com.metamapa.Domain.dto.EstadisticasDTO;
import com.metamapa.Domain.entities.ClienteAgregador;
import com.metamapa.Domain.entities.ExportadorCSV;
import com.metamapa.Domain.entities.InterfaceEstadistica;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceEstadistica {

    private final ClienteAgregador clienteAgregador;
    private EstadisticasDTO ultimasEstadisticas;
    private List<InterfaceEstadistica> estadisticas;

    public ServiceEstadistica(ClienteAgregador cliente){
        this.clienteAgregador = cliente;
    }

    @Cacheable("estadisticas")
    public EstadisticasDTO obtenerEstadisticas() {
        //VER que onda
        if(ultimasEstadisticas == null){
            actualizarUltimasEstadisticas();
        }
        return ultimasEstadisticas;
    }

    public String generarCSV() {
        EstadisticasDTO dto = obtenerEstadisticas();
        //ExportadorCSV exp = new ExportadorCSV();
        //return exp.obtenerArchivoTipo(dto);
        return "ewewe";
    }

    @Scheduled(fixedRate = 300000) // cada 5 minutos
    @CacheEvict(value = "estadisticas", allEntries = true)
    public void actualizarUltimasEstadisticas(){
        //ACÄ debería ir un foreach de todas las estadisticas que se quieran pedir
    }

}
