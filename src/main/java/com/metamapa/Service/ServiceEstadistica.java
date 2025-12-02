package com.metamapa.Service;

import com.metamapa.Domain.dto.output.EstadisticaOutputDTO;
import com.metamapa.Domain.entities.*;
import com.metamapa.Domain.entities.repository.IRepositoryEstadisticas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceEstadistica {

    @Autowired
    private final ClienteAgregador clienteAgregador;
    private List<InterfaceEstadistica> estadisticas = new ArrayList<>();
    private IRepositoryEstadisticas repo ;


    public ServiceEstadistica(ClienteAgregador cliente,IRepositoryEstadisticas repo){
        this.clienteAgregador = cliente;
        this.repo = repo;
    }
    public void actualizarResultadosEstadisticas() {
        if(this.estadisticas.isEmpty()){
            this.estadisticas = repo.findAll();
        }

        for (InterfaceEstadistica est : this.estadisticas) {
            est.actualizarResultado();        // llama al método propio de la clase
            repo.save(est);                   // guarda el nuevo resultado
        }

        System.out.println("Estadísticas actualizadas y persistidas");
    }
    @Cacheable("estadisticas")
    public List<EstadisticaOutputDTO> obtenerResultadosDeEstadisticas(long id_estadistica) {
        if(id_estadistica==0){
            return new EstadisticaOutputDTO(repo.findById(Long.toString(id_estadistica)));}
        return generarEstadisticaOutputDTO(repo.findAll());
           }

    private List<EstadisticaOutputDTO> generarEstadisticaOutputDTO(List<InterfaceEstadistica> estadisticas) {
        List<EstadisticaOutputDTO> estadisticasDTO = new ArrayList<>();
        estadisticas.forEach(estadistica -> {
            estadisticasDTO.add(new EstadisticaOutputDTO(estadistica));
        });
        return estadisticasDTO;

    }


    public String generarCSV(List<InterfaceEstadistica> estadisticas) {
        //EstadisticaOutputDTO dto = obtenerEstadisticas(estadisticas.get(1).getId_estadistica());
        //ExportadorCSV exp = new ExportadorCSV();
        //return exp.obtenerArchivoTipo(dto);
        return "ewewe";
    }

    public void actualizarEstadisticas() {
        List<String> colecciones ;
        List<String> categorias ;

        colecciones=this.clienteAgregador.obtenerColecciones();
        categorias = this.clienteAgregador.obtenerCategorias();

        //MEJORA
        if(estadisticas==null){
            //crearEstdisticas
            //crea estadistica relacionada a la coleccion
            for(String coleccion : colecciones){
                if(coleccion!=null) {
                    InterfaceEstadistica estadistica = this.crearEstadisticaColeccion(coleccion);
                    this.estadisticas.add(estadistica);
                }
            }
            //crea estdistica relacionada con categoria
            EstadisticaCategoriaMaxima estadisticaMaxCategori =new EstadisticaCategoriaMaxima();

            for(String categoria: categorias){
                if(categoria!=null){

                    //Estadistica hora  por categoria
                    InterfaceEstadistica estadistica = this.crearEstadicaHoraPorCategoria(categoria);
                    this.estadisticas.add(estadistica);
                    //Estadistica provincia por categoria
                    InterfaceEstadistica estadistica1=this.crearEstadiscaProvinciaPorCategoria(categoria);
                    this.estadisticas.add(estadistica1);
                }
            }

            // Estadistica Spam
            //EstadisticaSpamEliminacion estadisticaSpam = new EstadisticaSpamEliminacion();

            //Ejecutamos para agregar informacion a todas las estadisticas , y luego persistimos

            //this.estadisticas.stream().forEach(e-> e.actualizarEstadistica());
            //this.repo.saveAll(this.estadisticas);
            return;

        }
        //this.estadisticas.stream().forEach(e-> e.actualizarEstadistica());
        //.repo.saveAll(this.estadisticas);


        //La idea es que acà le pida las cosas al agregador, es decir dame todas las colecciones,
        //TODAS LAS PROVINCIa Y TODAS LAS CACTEGORIAS

        //dps me deberìa fijar si existe o noen la BBDD de las estadisticas, deberìa tener un atributo quesea activa?
        //Hay otra forma de hacerlo mas rapido?


        //Creo las nuevas estdaisticas y las envio en una lista y dps las añado en la lista del controller
    }

    private InterfaceEstadistica crearEstadiscaProvinciaPorCategoria(String categoria) {
        return new EstadisticaProvinciaPorCategoria(categoria);
    }

    private InterfaceEstadistica crearEstadicaHoraPorCategoria(String categoria) {
        return new EstadisticaHoraPorCategoria(categoria);
    }

    public InterfaceEstadistica crearEstadisticaColeccion(String coleccion){

       return new EstadisticaMaxHechosPorProvinciaDeUnaColeccion(coleccion);

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