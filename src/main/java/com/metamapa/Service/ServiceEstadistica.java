package com.metamapa.Service;

import com.metamapa.Config.EstadisticaUpdateMarker;
import com.metamapa.Domain.FactoryEstadisticaDTO;
import com.metamapa.Domain.dto.output.EstadisticaOutputDTO;
import com.metamapa.Domain.entities.*;
import com.metamapa.Domain.entities.repository.IRepositoryEstadisticas;
import com.metamapa.Domain.entities.repository.RepositoryEstadisticaUpdate;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceEstadistica {

    private final ClienteAgregador clienteAgregador;
    private List<InterfaceEstadistica> estadisticas = new ArrayList<>();
    private IRepositoryEstadisticas repo ;
    private IExportador exportador;
    private RepositoryEstadisticaUpdate repoUpdate;
    private LocalDateTime ultimoUpdateLocal;
    private FactoryEstadisticaDTO factoryEstadistica;


    public ServiceEstadistica(ClienteAgregador cliente,IRepositoryEstadisticas repo, RepositoryEstadisticaUpdate repoUpdate,IExportador exportador,FactoryEstadisticaDTO factory) {
        this.clienteAgregador = cliente;
        this.repo = repo;
        this.repoUpdate = repoUpdate;
        //Ver si este va
        this.exportador= exportador;
        this.factoryEstadistica = factory;
        //this.ultimoUpdateLocal= repoUpdate.findById("singleton").orElse(null).getLastUpdate();

    }
    public synchronized void actualizarResultadosEstadisticas() {
        if(this.estadisticas.isEmpty()){
            this.estadisticas = repo.findAll();
        }

        for (InterfaceEstadistica est : this.estadisticas) {
            est.actualizarResultado();        // llama al método propio de la clase
            repo.save(est);                   // guarda el nuevo resultado
        }
        this.ultimoUpdateLocal = LocalDateTime.now();
        System.out.println("Estadísticas actualizadas y persistidas");
    }

    //Debería tener una lista o está bien que los vaya a buscar siempre?
    //x ahí es mejor que ya los tenga calculados
    @CacheEvict(value="estadisticas", allEntries = true)
    //DEBRIA SER UNA LISTA -- DEBERIA CONSIDEREARSE A CAMBIARSE A SOLO EatdisticaOutputDTO
    public List<EstadisticaOutputDTO> obtenerResultadosDeEstadisticas() {
        //MEJORAR
        return this.factoryEstadistica.crearListaEstadisticaDTO(obtener());
    }


    public EstadisticaOutputDTO obtenerResultadoPorID(String idEstadistica) {

        InterfaceEstadistica est = obtener().stream()
                .filter(e -> e.getId().equals(idEstadistica))
                .findFirst()
                .orElse(null);

        return est == null ? null : factoryEstadistica.crearEstadisticaDTO(est);
    }

    public String generarCSV() {
        List<InterfaceEstadistica> estadisticas = this.obtener();
        String estadisticaCSV ="";
        for(InterfaceEstadistica est :estadisticas ){
            String aExportar = this.exportador.exportar(est);
            estadisticaCSV +=aExportar;

        }

        return estadisticaCSV;
    }

    public synchronized void actualizarEstadisticas() {
        List<String> colecciones = this.clienteAgregador.obtenerColecciones();
        List<String> categorias = this.clienteAgregador.obtenerCategorias();
        this.estadisticas = new ArrayList<>();
        repo.deleteAll();
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


            // Estadistica Spam
            //EstadisticaSpamEliminacion estadisticaSpam = new EstadisticaSpamEliminacion();

            //Ejecutamos para agregar informacion a todas las estadisticas , y luego persistimos

            //this.estadisticas.stream().forEach(e-> e.actualizarEstadistica());
            //this.repo.saveAll(this.estadisticas)

        }
        repo.saveAll(this.estadisticas);
        EstadisticaUpdateMarker marker = new EstadisticaUpdateMarker();
        marker.setLastUpdate(LocalDateTime.now());
        repoUpdate.save(marker);

        this.ultimoUpdateLocal = marker.getLastUpdate();
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
    @PostConstruct
    public synchronized void cargarEstadisticasDesdeDB() {
        this.estadisticas = repo.findAll();
        EstadisticaUpdateMarker marker =
                repoUpdate.findById("singleton").orElse(null);

        this.ultimoUpdateLocal = marker != null
                ? marker.getLastUpdate()
                : LocalDateTime.MIN;
    }

    public synchronized void refrescarSiEsNecesario() {

        EstadisticaUpdateMarker marker =
                repoUpdate.findById("singleton").orElse(null);

        if (marker == null) return;

        if (ultimoUpdateLocal == null ||
                marker.getLastUpdate().isAfter(ultimoUpdateLocal)) {

            this.estadisticas = repo.findAll();
            this.ultimoUpdateLocal = marker.getLastUpdate();
        }
    }


    public List<InterfaceEstadistica> obtener() {
        refrescarSiEsNecesario();
        return this.estadisticas;
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