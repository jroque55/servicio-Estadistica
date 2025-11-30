package com.metamapa.Service;

import com.metamapa.Domain.dto.output.EstadisticaOutputDTO;
import com.metamapa.Domain.entities.ClienteAgregador;
import com.metamapa.Domain.entities.EstadisticaSpamEliminacion;
import com.metamapa.Domain.entities.InterfaceEstadistica;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceEstadistica {

    private final ClienteAgregador clienteAgregador;
    private List<InterfaceEstadistica> estadisticas = new ArrayList<>();
    //categoriasVigentes;
    //coleccionesVigentes;

    public ServiceEstadistica(ClienteAgregador cliente){
        this.clienteAgregador = cliente;
    }

    @Cacheable("estadisticas")
    public List<EstadisticaOutputDTO> obtenerEstadisticas(long id_estadistica) {
        //Primero reviso si hay o no IdESTADISTICA asì solo mando uno

        //Si no deberìa mandarle todas las cosas :D


        //Reppository de estadistica que traiga segùn el Id
        //this.estadisticas.add(new EstadisticaSpamEliminacion());
        //this.estadisticas =actualizarUltimasEstadisticas();
        //return generarEstadisticaOutputDTO();
        return new ArrayList<>();
           }


    public String generarCSV(List<InterfaceEstadistica> estadisticas) {
        //EstadisticaOutputDTO dto = obtenerEstadisticas(estadisticas.get(1).getId_estadistica());
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
        estadisticas.add(new EstadisticaSpamEliminacion());
        this.estadisticas.stream().forEach(a-> a.actualizarEstadistica());
        return null;//ACÄ debería ir un foreach de todas las estadisticas que se quieran pedir
    }

    public void actualizarEstadisticas() {
        List<String> colecciones ;
        List<String> provincias ;
        List<String> categorias ;

        colecciones=this.clienteAgregador.obtenerColecciones();
        provincias = this.clienteAgregador.obtenerProvincias();
        categorias = this.clienteAgregador.obtenerCategorias();
        //La idea es que acà le pida las cosas al agregador, es decir dame todas las colecciones,
        //TODAS LAS PROVINCIa Y TODAS LAS CACTEGORIAS

        //dps me deberìa fijar si existe o noen la BBDD de las estadisticas, deberìa tener un atributo quesea activa?
        //Hay otra forma de hacerlo mas rapido?


        //Creo las nuevas estdaisticas y las envio en una lista y dps las añado en la lista del controller
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