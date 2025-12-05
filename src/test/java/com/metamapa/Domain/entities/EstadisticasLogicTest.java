package com.metamapa.Domain.entities;

import com.metamapa.Domain.dto.input.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class EstadisticasLogicTest {

    static class TestCliente extends ClienteAgregador {
        public TestCliente() {
            super(org.springframework.web.reactive.function.client.WebClient.builder());
        }

        @Override
        public List<ProvCatDTO> obtenerCantHechosPorProvinciaSegun(String categoria) {
            return Arrays.asList(new ProvCatDTO("A", 5L), new ProvCatDTO("B", 10L));
        }

        @Override
        public List<CatHourDTO> obtenerHechosPorHoraSegun(String categoria) {
            return Arrays.asList(new CatHourDTO(1, 2L), new CatHourDTO(2, 7L));
        }

        @Override
        public List<ProvinceDTO> obtenerCantHechosXProvinciaDe(String nombreColeccion) {
            return Arrays.asList(new ProvinceDTO("X", 3L), new ProvinceDTO("Y", 8L));
        }

        @Override
        public List<CategoryDTO> obtenerCantHechosPorCategoria() {
            return Arrays.asList(new CategoryDTO("c1", 2L), new CategoryDTO("c2", 20L));
        }

        @Override
        public SpamSummaryDTO obtenerDatosSolicitudesSpam() {
            return new SpamSummaryDTO(100L, 15L);
        }
    }

    static class TestClienteVacio extends ClienteAgregador {
        public TestClienteVacio() {
            super(org.springframework.web.reactive.function.client.WebClient.builder());
        }

        @Override
        public List<ProvCatDTO> obtenerCantHechosPorProvinciaSegun(String categoria) {
            return Collections.emptyList();
        }

        @Override
        public List<CatHourDTO> obtenerHechosPorHoraSegun(String categoria) {
            return Collections.emptyList();
        }

        @Override
        public List<ProvinceDTO> obtenerCantHechosXProvinciaDe(String nombreColeccion) {
            return Collections.emptyList();
        }

        @Override
        public List<CategoryDTO> obtenerCantHechosPorCategoria() {
            return Collections.emptyList();
        }

        @Override
        public SpamSummaryDTO obtenerDatosSolicitudesSpam() {
            return null;
        }
    }

    @BeforeEach
    void setup() {
        // aseguramos que antes de cada test el singleton esté inicializado por seguridad
        new TestCliente();
    }

    @Test
    void provinciaPorCategoria_calculaMax_correctamente() {
        EstadisticaProvinciaPorCategoria est = new EstadisticaProvinciaPorCategoria("any");
        est.actualizarResultado();
        ProvCatDTO res = est.getResultado();
        assertNotNull(res, "Resultado no debe ser nulo cuando hay datos");
        assertEquals("B", res.getProvincia());
        assertEquals(10L, res.getCantidad());
    }

    @Test
    void provinciaPorCategoria_conListaVacia_resultadoNulo() {
        new TestClienteVacio();
        EstadisticaProvinciaPorCategoria est = new EstadisticaProvinciaPorCategoria("any");
        est.actualizarResultado();
        assertNull(est.getResultado());
    }

    @Test
    void horaPorCategoria_calculaMax_correctamente() {
        EstadisticaHoraPorCategoria est = new EstadisticaHoraPorCategoria("any");
        est.actualizarResultado();
        CatHourDTO res = est.getResultado();
        assertNotNull(res);
        assertEquals(2, res.getHora());
        assertEquals(7L, res.getCantidad());
    }

    @Test
    void horaPorCategoria_conListaVacia_resultadoNulo() {
        new TestClienteVacio();
        EstadisticaHoraPorCategoria est = new EstadisticaHoraPorCategoria("any");
        est.actualizarResultado();
        assertNull(est.getResultado());
    }

    @Test
    void maxHechosPorProvincia_deColeccion_calculaMax_correctamente() {
        EstadisticaMaxHechosPorProvinciaDeUnaColeccion est = new EstadisticaMaxHechosPorProvinciaDeUnaColeccion("colec");
        est.actualizarResultado();
        ProvinceDTO res = est.getResultado();
        assertNotNull(res);
        assertEquals("Y", res.getProvincia());
        assertEquals(8L, res.getCantidad());
    }

    @Test
    void maxHechosPorProvincia_deColeccion_listaVacia_resultadoNulo() {
        new TestClienteVacio();
        EstadisticaMaxHechosPorProvinciaDeUnaColeccion est = new EstadisticaMaxHechosPorProvinciaDeUnaColeccion("colec");
        est.actualizarResultado();
        assertNull(est.getResultado());
    }

    @Test
    void categoriaMaxima_calculaMax_correctamente() {
        EstadisticaCategoriaMaxima est = new EstadisticaCategoriaMaxima();
        est.actualizarResultado();
        CategoryDTO res = est.getResultado();
        assertNotNull(res);
        assertEquals("c2", res.getCategoria());
        assertEquals(20L, res.getCantidad());
    }

    @Test
    void categoriaMaxima_listaVacia_resultadoNulo() {
        new TestClienteVacio();
        EstadisticaCategoriaMaxima est = new EstadisticaCategoriaMaxima();
        est.actualizarResultado();
        assertNull(est.getResultado());
    }

    @Test
    void spamEliminacion_actualizaCampos_correctamente() {
        EstadisticaSpamEliminacion est = new EstadisticaSpamEliminacion();
        est.actualizarResultado();
        assertEquals(15L, est.getResultado());
        assertEquals(100L, est.getCantidadTotal());
    }

    @Test
    void spamEliminacion_conClienteVacio_resultadoNoSetea() {
        new TestClienteVacio();
        EstadisticaSpamEliminacion est = new EstadisticaSpamEliminacion();
        est.actualizarResultado();
        assertNull(est.getResultado());
        assertNull(est.getCantidadTotal());
    }
}

