package com.metamapa.Service;

import com.metamapa.Domain.dto.EstadisticasDTO;
import com.metamapa.Domain.entities.ClienteAgregador;
import com.metamapa.Domain.entities.ExportadorCSV;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ServiceEstadistica {

    private final ClienteAgregador clienteAgregador;
    private EstadisticasDTO ultimasEstadisticas;

    public ServiceEstadistica(ClienteAgregador cliente){
        this.clienteAgregador = cliente;
    }

    @Cacheable("estadisticas")
    public EstadisticasDTO obtenerEstadisticas() {
        if(ultimasEstadisticas == null){
            actualizarUltimasEstadisticas();
        }
        return ultimasEstadisticas;
    }

    public String generarCSV() {
        EstadisticasDTO dto = obtenerEstadisticas();
        ExportadorCSV exp = new ExportadorCSV();
        return exp.obtenerArchivoTipo(dto);
    }

    @Scheduled(fixedRate = 300000) // cada 5 minutos
    @CacheEvict(value = "estadisticas", allEntries = true)
    public void actualizarUltimasEstadisticas(){
        this.ultimasEstadisticas = clienteAgregador.obtenerEstadisticasAgregador();
    }

}
