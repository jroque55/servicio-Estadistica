package com.metamapa.Domain.entities;

import com.metamapa.Domain.dto.input.CatHourDTO;
import lombok.Data;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

import static com.metamapa.Domain.entities.EnumTipoEstadistica.MAXHORASEGUNCATEGORIA;


@Data
@Document(collection = "estadisticas")
@TypeAlias("estadistica_horaXCategoria")
public class EstadisticaHoraPorCategoria extends InterfaceEstadistica {
    private Integer cantidad;
    private List<CatHourDTO> cantidadXHoras;
    private CatHourDTO resultado;

    public EstadisticaHoraPorCategoria(String categoria) {
        this.setDiscriminante(new Discriminante(EnumTipoDiscriminante.CATEGORIA,categoria ));
        this.setTipoEstadistica(MAXHORASEGUNCATEGORIA);

    }

    public void actualizarEstadistica() {

        // obtengo singleton ClienteAgregador
        ClienteAgregador cliente = getClienteAgregador();

        if (cliente == null) {
            // no hay cliente disponible -> no se puede calcular
            return;
        }

        try {
            this.cantidadXHoras = cliente.obtenerHechosPorHoraSegun(this.getDiscriminante().getValor());
        } catch (Exception ex) {
            return;
        }
        // delegar el cálculo a un método separado
        CalcularResultado(this.cantidadXHoras);
    }

    // Mueve la lógica de cálculo de resultado aquí (solicitado entre líneas 47 y 63)
    private void CalcularResultado(List<CatHourDTO> cantidadXHoras) {
        if (cantidadXHoras == null || cantidadXHoras.isEmpty()) {
            this.setResultado("No hay hechos con la categoria " + this.getDiscriminante().getValor());
            return;
        }

        int maxCantidad = 0;
        String horaMaxCategoria = null;
        // recorrer y buscar la hora con mayor cantidad
        for (CatHourDTO horaraw : cantidadXHoras) {
            if (horaraw == null) continue;

            Long cant = horaraw.getCantidad();
            int cantidadActual = (cant == null) ? 0 : cant.intValue();

            if (cantidadActual > maxCantidad) {
                maxCantidad = cantidadActual;
                horaMaxCategoria = (horaraw.getHora() == null) ? null : horaraw.getHora().toString();
            }
        }

        this.setResultado(horaMaxCategoria);
        this.setCantidad(maxCantidad);
    }
}
