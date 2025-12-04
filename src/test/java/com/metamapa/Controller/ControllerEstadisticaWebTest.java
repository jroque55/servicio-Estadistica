package com.metamapa.Controller;

import com.metamapa.Service.ServiceEstadistica;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

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
}

