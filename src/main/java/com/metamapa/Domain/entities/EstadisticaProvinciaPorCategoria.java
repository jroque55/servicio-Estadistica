package com.metamapa.Domain.entities;

import com.metamapa.Domain.dto.input.ProvCatDTO;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
@Entity
@DiscriminatorValue("MAXPROVINCIASEGUNCONCATEGORIA")
public class EstadisticaProvinciaPorCategoria extends InterfaceEstadistica {
    private final String categoria;
    private Integer cantidad;
    // mapa provincia -> cantidad de hechos
   // private final Map<String, Integer> provinciaConteo = new HashMap<>();

    public EstadisticaProvinciaPorCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void actualizarResultado() {

        // obtener el singleton ClienteAgregador
        ClienteAgregador cliente = getClienteAgregador();

        if (cliente == null) {
            // no hay cliente disponible -> no se puede calcular
            return;
        }

        List<ProvCatDTO> datos;
        try {
            datos = cliente.obtenerEstadisticaAgregador(categoria,"provincia");
        } catch (Exception ex) {
            return ;
        }

        if (datos == null || datos.isEmpty()) {
            this.setResultado("No hay hechos con la categoria"+this.categoria);
            return;
        }

        int maxCantidad = 0;
        String provinciaMaxCategoria = null;
        //MEJORAR: TODO
       // datos.stream().max()
        for (ProvCatDTO provinciaRaw : datos) {
            if (provinciaRaw == null) continue;

            int cantidadActual = provinciaRaw.getCantidad().intValue();

            if (cantidadActual > maxCantidad) {
                maxCantidad = cantidadActual;
                provinciaMaxCategoria = provinciaRaw.getProvincia();
            }
        }
        this.setResultado(provinciaMaxCategoria);
        this.setCantidad(maxCantidad);

        // construir resultado (opcional: mostrar el conteo)
//        if (!provinciaConteo.isEmpty()) {
//            setResultado("Conteo de provincias para categoría '" + categoria + "': " + provinciaConteo);
//        }
    }
/*
    public Map<String, Integer> getProvinciaConteo() {
        return provinciaConteo;
    }

    public String getCategoria() {
        return categoria;
    }

 */
}
