package com.metamapa.Domain.entities;

import com.metamapa.Domain.dto.input.CatHourDTO;
import lombok.Data;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Comparator;
import java.util.List;

import static com.metamapa.Domain.entities.EnumTipoEstadistica.MAXHORASEGUNCATEGORIA;


@Data
@Document(collection = "estadisticas")
@TypeAlias("estadistica_horaXCategoria")
public class EstadisticaHoraPorCategoria extends InterfaceEstadistica {
    //private Integer cantidad;
    private List<CatHourDTO> cantidadXHoras;
    private CatHourDTO resultado;

    public EstadisticaHoraPorCategoria(String categoria) {
        this.setDiscriminante(new Discriminante(EnumTipoDiscriminante.CATEGORIA,categoria ));
        this.setTipoEstadistica(MAXHORASEGUNCATEGORIA);
    }
    public EstadisticaHoraPorCategoria(){
        this.setTipoEstadistica(MAXHORASEGUNCATEGORIA);
    }

    @Override
    public void actualizarResultado() {

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
            this.setResultado(null);
            //this.setResultado("No hay hechos con la categoria " + this.getDiscriminante().getValor());
            return;
        }

        cantidadXHoras.sort(Comparator.comparing(CatHourDTO::getCantidad).reversed());

        this.setResultado(cantidadXHoras.get(0));
        //this.setCantidad(maxCantidad);
    }
}
