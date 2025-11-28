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
import java.util.function.ToDoubleBiFunction;

@Service
public class ServiceEstadistica {

    private final ClienteAgregador clienteAgregador;
    private EstadisticasDTO ultimasEstadisticas;
    private List<InterfaceEstadistica> estadisticas;
    //categoriasVigentes;
    //coleccionesVigentes;

    public ServiceEstadistica(ClienteAgregador cliente){
        this.clienteAgregador = cliente;
    }

    @Cacheable("estadisticas")
    public EstadisticasDTO obtenerEstadisticas() {
        //VER que onda
        this.estadisticas =actualizarUltimasEstadisticas();

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
    public List<InterfaceEstadistica> actualizarUltimasEstadisticas(){
        //obtenerCategorias
        //obtenerColecciones

        if(estadisticas==null){
            //crearEstdisticas
        }
        /*implementar logica si agregan o eliminan categorias o coleciones
         para poder tener estadisticas actualizadas */

        //Logica TODO

        this.estadisticas.stream().forEach(a-> a.actualizarResultado());
        return null;//ACÄ debería ir un foreach de todas las estadisticas que se quieran pedir
    }

}


/* Logica para despues eliminar o agregar estadisticas:
public class DiferenciaListas {
    public static void main(String[] args) {
        List<String> lista1 = new ArrayList<>(List.of("A", "B", "C"));
        List<String> lista2 = List.of("B", "C", "D");

        // elementos únicos de lista1
        List<String> noRepetidos = new ArrayList<>(lista1);
        noRepetidos.removeAll(lista2); // elimina B y C
        System.out.println("Unicos en lista1: " + noRepetidos); // [A]
---------------Este lo utilizaria para eliminar estadisticas viejas ------------
        // elementos únicos de lista2
        List<String> soloLista2 = new ArrayList<>(lista2);
        soloLista2.removeAll(lista1); // elimina B y C
        System.out.println("Unicos en lista2: " + soloLista2); // [D]
--------------Este loutilizaria para poder agregar nuevas estadisticas------------
*
*
* */