package com.metamapa.Domain.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document(collection = "estadisticas")
@Getter
@Setter
public abstract class InterfaceEstadistica {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_estadistica;
    private String resultado;  //Sería el numero
    private EnumTipoEstadistica tipoEstadistica;
    private Discriminante discriminante;
    //private String descripcion; //Sería el nombre de lo que diferecia la estadística


    // constante compartida que usan algunas implementaciones //Esto no lo entiendo mucho q digamos
    public static final String RESULTADO = "Sin resultado";

    // Helper para acceder al singleton ClienteAgregador desde las subclases
    protected ClienteAgregador getClienteAgregador() {
        return ClienteAgregador.getInstance();
    }

    // obligar a las subclases a implementar la lógica de actualización
    public abstract void actualizarEstadistica();

}
