package com.metamapa.Service;

import com.metamapa.Config.EstadisticaUpdateMarker;
import com.metamapa.Domain.FactoryEstadisticaDTO;
import com.metamapa.Domain.dto.input.CategoryDTO;
import com.metamapa.Domain.dto.output.EstadisticaOutputDTO;
import com.metamapa.Domain.entities.*;
import com.metamapa.Domain.entities.repository.IRepositoryEstadisticas;
import com.metamapa.Domain.entities.repository.RepositoryEstadisticaUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServiceEstadisticaTest {
/*
    static class RepoMock implements IRepositoryEstadisticas {
        private List<InterfaceEstadistica> almacen = Collections.emptyList();

        @Override
        public <S extends InterfaceEstadistica> S save(S entity) {
            return entity;
        }

        @Override
        public <S extends InterfaceEstadistica> List<S> saveAll(Iterable<S> entities) {
            //noinspection unchecked
            almacen = (List<InterfaceEstadistica>) entities;
            return (List<S>) almacen;
        }

        @Override
        public List<InterfaceEstadistica> findAll() {
            return almacen;
        }

        // demas metodos no usados: implementaciones por defecto que lanzan UnsupportedOperationException
        @Override public void deleteAll() {}
        // el resto se quedan sin implementar porque no se usan en las pruebas
        // ...

        // Métodos no implementados omitidos para brevedad (no usados en tests)
    }

    static class RepoUpdateMock implements RepositoryEstadisticaUpdate {
        private EstadisticaUpdateMarker marker;

        @Override
        public <S extends EstadisticaUpdateMarker> S save(S entity) {
            this.marker = entity;
            return entity;
        }

        @Override
        public java.util.Optional<EstadisticaUpdateMarker> findById(String s) {
            return java.util.Optional.ofNullable(marker);
        }

        // übrige Methoder nicht implementiert
    }

    static class ExportadorMock implements IExportador {
        @Override
        public String exportar(InterfaceEstadistica estaditica) {
            return "CSV:" + estaditica.getTipoEstadistica();
        }
    }

    static class ClientMock extends ClienteAgregador {
        public ClientMock() { super(org.springframework.web.reactive.function.client.WebClient.builder()); }

        @Override
        public List<String> obtenerColecciones() { return Arrays.asList("col1","col2"); }

        @Override
        public List<String> obtenerCategorias() { return Arrays.asList("cat1","cat2"); }
    }

    @BeforeEach
    void setup() {
        new ClientMock();
    }

    @Test
    void actualizarEstadisticas_creaEstadisticas_segunColeccionesYCategorias() {
        IRepositoryEstadisticas repo = new RepoMock();
        RepositoryEstadisticaUpdate repoUpdate = new RepoUpdateMock();
        ServiceEstadistica service = new ServiceEstadistica(ClienteAgregador.getInstance(), repo, repoUpdate, new ExportadorMock(), new FactoryEstadisticaDTO());

        service.actualizarEstadisticas();

        List<InterfaceEstadistica> lista = service.obtener();
        assertNotNull(lista);
        // deberia crear 2 colecciones + 2 categorias * 2 tipos = 6? El codigo crea: por cada coleccion 1 estadistica, por cada categoria 2 estadisticas
        assertEquals(2 + 2*2, lista.size());

    }

    @Test
    void generarCSV_laConcatenaResultadosExportador() {
        IRepositoryEstadisticas repo = new RepoMock();
        RepoUpdateMock repoUpdate = new RepoUpdateMock();
        ServiceEstadistica service = new ServiceEstadistica(ClienteAgregador.getInstance(), repo, repoUpdate, new ExportadorMock(), new FactoryEstadisticaDTO());

        // preparar lista manual de estadisticas
        List<InterfaceEstadistica> estadisticas = Arrays.asList(
                new EstadisticaCategoriaMaxima(),
                new EstadisticaHoraPorCategoria("c"),
                new EstadisticaMaxHechosPorProvinciaDeUnaColeccion("co")
        );

        // inyectar internamente
        service.obtener().clear();
        service.obtener().addAll(estadisticas);

        String csv = service.generarCSV();
        assertTrue(csv.contains("CSV:"));
        assertTrue(csv.contains("MAXCATEGORIACONHECHOS"));
    }

    @Test
    void obtenerResultadosDeEstadisticas_filtraDatosVacios() {
        IRepositoryEstadisticas repo = new RepoMock();
        RepoUpdateMock repoUpdate = new RepoUpdateMock();
        ServiceEstadistica service = new ServiceEstadistica(ClienteAgregador.getInstance(), repo, repoUpdate, new ExportadorMock(), new FactoryEstadisticaDTO());

        EstadisticaCategoriaMaxima estConDatos = new EstadisticaCategoriaMaxima();
        CategoryDTO cat = new CategoryDTO("cX", 1L);
        estConDatos.setCategorias(Arrays.asList(cat));

        EstadisticaCategoriaMaxima estSinDatos = new EstadisticaCategoriaMaxima();

        service.obtener().clear();
        service.obtener().add(estConDatos);
        service.obtener().add(estSinDatos);

        List<EstadisticaOutputDTO> resultados = service.obtenerResultadosDeEstadisticas();
        assertEquals(1, resultados.size());
    }

    @Test
    void obtenerResultadoPorID_devuelveDTOoNullSegunID() {
        IRepositoryEstadisticas repo = new RepoMock();
        RepoUpdateMock repoUpdate = new RepoUpdateMock();
        ServiceEstadistica service = new ServiceEstadistica(ClienteAgregador.getInstance(), repo, repoUpdate, new ExportadorMock(), new FactoryEstadisticaDTO());

        EstadisticaCategoriaMaxima est = new EstadisticaCategoriaMaxima();
        est.setId("abc");
        service.obtener().clear();
        service.obtener().add(est);

        assertNotNull(service.obtenerResultadoPorID("abc"));
        assertNull(service.obtenerResultadoPorID("nope"));
    } */

}

