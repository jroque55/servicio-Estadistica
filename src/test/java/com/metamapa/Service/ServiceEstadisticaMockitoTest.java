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
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
class ServiceEstadisticaMockitoTest {

    static class ExportadorMock implements IExportador {
        @Override
        public String exportar(InterfaceEstadistica estaditica) {
            return "CSV:" + estaditica.getTipoEstadistica();
        }
    }

    static class ClientMock extends ClienteAgregador {
        public ClientMock() {
            super(org.springframework.web.reactive.function.client.WebClient.builder());
        }

        @Override
        public List<String> obtenerColecciones() {
            return Arrays.asList("col1", "col2");
        }

        @Override
        public List<String> obtenerCategorias() {
            return Arrays.asList("cat1", "cat2");
        }
    }

    private ServiceEstadistica service;
    private IRepositoryEstadisticas repo;
    private RepositoryEstadisticaUpdate repoUpdate;

    @BeforeEach
    void setup() {
        // Inicializa el singleton mockeado
        new ClientMock();

        repo = Mockito.mock(IRepositoryEstadisticas.class);
        repoUpdate = Mockito.mock(RepositoryEstadisticaUpdate.class);

        when(repo.saveAll(any())).thenAnswer(invocation -> {
            Iterable<InterfaceEstadistica> it = invocation.getArgument(0);
            List<InterfaceEstadistica> lista = new ArrayList<>();
            it.forEach(lista::add);
            when(repo.findAll()).thenReturn(lista);
            return lista;
        });

        doNothing().when(repo).deleteAll();

        when(repoUpdate.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service = new ServiceEstadistica(
                ClienteAgregador.getInstance(),
                repo,
                repoUpdate,
                new ExportadorMock(),
                new FactoryEstadisticaDTO()
        );
    }


    @Test
    void creaEstadisticasUnicas() {
        service.actualizarEstadisticas();

        assertTrue(
                service.obtener().stream()
                        .anyMatch(e -> e instanceof EstadisticaCategoriaMaxima)
        );

        assertTrue(
                service.obtener().stream()
                        .anyMatch(e -> e instanceof EstadisticaSpamEliminacion)
        );
    }

    @Test
    void actualizarEstadisticas_creaTodasLasEstadisticasEsperadas() {
        service.actualizarEstadisticas();

        List<InterfaceEstadistica> lista = service.obtener();

        assertEquals(8, lista.size());
    }
    @Test
    void creaEstadisticasPorColeccion() {
        service.actualizarEstadisticas();

        long count = service.obtener().stream()
                .filter(e -> e instanceof EstadisticaMaxHechosPorProvinciaDeUnaColeccion)
                .count();

        assertEquals(2, count);
    }

    @Test
    void creaEstadisticasPorCategoria() {
        service.actualizarEstadisticas();

        long hora = service.obtener().stream()
                .filter(e -> e instanceof EstadisticaHoraPorCategoria)
                .count();

        long provincia = service.obtener().stream()
                .filter(e -> e instanceof EstadisticaProvinciaPorCategoria)
                .count();

        assertEquals(2, hora);
        assertEquals(2, provincia);
    }



    @Test
    void generarCSV_laConcatenaResultadosExportador() {
        IRepositoryEstadisticas repo = Mockito.mock(IRepositoryEstadisticas.class);
        RepositoryEstadisticaUpdate repoUpdate = Mockito.mock(RepositoryEstadisticaUpdate.class);
        ServiceEstadistica service = new ServiceEstadistica(ClienteAgregador.getInstance(), repo, repoUpdate, new ExportadorMock(), new FactoryEstadisticaDTO());

        List<InterfaceEstadistica> estadisticas = Arrays.asList(
                new EstadisticaCategoriaMaxima(),
                new EstadisticaHoraPorCategoria("c"),
                new EstadisticaMaxHechosPorProvinciaDeUnaColeccion("co")
        );

        service.obtener().clear();
        service.obtener().addAll(estadisticas);

        String csv = service.generarCSV();
        assertTrue(csv.contains("CSV:"));
        assertTrue(csv.contains("MAXCATEGORIACONHECHOS"));
    }

    @Test
    void obtenerResultadosDeEstadisticas_filtraDatosVacios() {
        IRepositoryEstadisticas repo = Mockito.mock(IRepositoryEstadisticas.class);
        RepositoryEstadisticaUpdate repoUpdate = Mockito.mock(RepositoryEstadisticaUpdate.class);
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
        IRepositoryEstadisticas repo = Mockito.mock(IRepositoryEstadisticas.class);
        RepositoryEstadisticaUpdate repoUpdate = Mockito.mock(RepositoryEstadisticaUpdate.class);
        ServiceEstadistica service = new ServiceEstadistica(ClienteAgregador.getInstance(), repo, repoUpdate, new ExportadorMock(), new FactoryEstadisticaDTO());

        EstadisticaCategoriaMaxima est = new EstadisticaCategoriaMaxima();
        est.setId("abc");
        service.obtener().clear();
        service.obtener().add(est);

        assertNotNull(service.obtenerResultadoPorID("abc"));
        assertNull(service.obtenerResultadoPorID("nope"));
    }

}

