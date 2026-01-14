package com.metamapa.Controller;

import com.metamapa.Domain.dto.output.DatoDTO;
import com.metamapa.Domain.dto.output.DiscriminanteDTO;
import com.metamapa.Domain.dto.output.EstadisticaOutputDTO;
import com.metamapa.Service.ServiceEstadistica;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ControllerEstadistica.class)
class ControllerEstadisticaWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceEstadistica serviceEstadistica;

    @Test
    void exportarCSV_devuelveCSVOK() throws Exception {

        Mockito.when(serviceEstadistica.generarCSV())
                .thenReturn("col1,col2\nA,B");

        mockMvc.perform(get("/estadisticas/exportar"))
                .andExpect(status().isOk())
                .andExpect(header().string(
                        "Content-Disposition",
                        "attachment; filename=estadisticas.csv"
                ))
                .andExpect(content().string("col1,col2\nA,B"));
    }
    @Test
    void obtenerEstadisticas_listaConMultiplesEstadisticas_tieneEstructuraCorrecta() throws Exception {

        // ========= ESTADISTICA 1 =========
        DatoDTO resultado1 = new DatoDTO();
        resultado1.setNombre("cat1");
        resultado1.setCantidad(10L);

        DatoDTO e1d1 = new DatoDTO();
        e1d1.setNombre("08:00");
        e1d1.setCantidad(3L);

        DatoDTO e1d2 = new DatoDTO();
        e1d2.setNombre("09:00");
        e1d2.setCantidad(7L);

        DiscriminanteDTO disc1 = new DiscriminanteDTO();
        disc1.setTipo("EstadisticaHoraPorCategoria");
        disc1.setValor("09:00");

        EstadisticaOutputDTO dto1 = new EstadisticaOutputDTO();
        dto1.setDescripcion("Hechos por hora");
        dto1.setResultado(resultado1);
        dto1.setDatos(List.of(e1d1, e1d2));
        dto1.setDiscriminante(disc1);

        // ------------ESTADISTICA 2 --------
        DatoDTO resultado2 = new DatoDTO();
        resultado2.setNombre("cat2");
        resultado2.setCantidad(5L);

        DatoDTO e2d1 = new DatoDTO();
        e2d1.setNombre("Buenos Aires");
        e2d1.setCantidad(2L);

        DatoDTO e2d2 = new DatoDTO();
        e2d2.setNombre("Córdoba");
        e2d2.setCantidad(3L);

        DiscriminanteDTO disc2 = new DiscriminanteDTO();
        disc2.setTipo("EstadisticaProvinciaPorCategoria");
        disc2.setValor("ARG");

        EstadisticaOutputDTO dto2 = new EstadisticaOutputDTO();
        dto2.setDescripcion("Hechos por provincia");
        dto2.setResultado(resultado2);
        dto2.setDatos(List.of(e2d1, e2d2));
        dto2.setDiscriminante(disc2);

        // ---------MOCK SERVICE -----------
        Mockito.when(serviceEstadistica.obtenerResultadosDeEstadisticas())
                .thenReturn(List.of(dto1, dto2));


        mockMvc.perform(get("/estadisticas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))

                // verificacion en la lista
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))

                //verificacion en estadistica 1
                .andExpect(jsonPath("$[0].descripcion").value("Hechos por hora"))
                .andExpect(jsonPath("$[0].resultado.nombre").value("cat1"))
                .andExpect(jsonPath("$[0].resultado.cantidad").value(10))
                .andExpect(jsonPath("$[0].datos.length()").value(2))
                .andExpect(jsonPath("$[0].discriminante.tipo").value("EstadisticaHoraPorCategoria"))
                .andExpect(jsonPath("$[0].discriminante.valor").value("09:00"))

                // verificacion en estadistica 2
                .andExpect(jsonPath("$[1].descripcion").value("Hechos por provincia"))
                .andExpect(jsonPath("$[1].resultado.nombre").value("cat2"))
                .andExpect(jsonPath("$[1].resultado.cantidad").value(5))
                .andExpect(jsonPath("$[1].datos.length()").value(2))
                .andExpect(jsonPath("$[1].discriminante.tipo").value("EstadisticaProvinciaPorCategoria"))
                .andExpect(jsonPath("$[1].discriminante.valor").value("ARG"));
    }


}
