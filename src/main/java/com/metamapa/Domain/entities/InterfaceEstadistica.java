package com.metamapa.Domain.entities;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "estadisticas")
@Data
public abstract class InterfaceEstadistica {
    @Id
    private String id;
    private EnumTipoEstadistica tipoEstadistica;
    private Discriminante discriminante;
    //private String descripcion; //Sería el nombre de lo que diferecia la estadística


    // constante compartida que usan algunas implementaciones //Esto no lo entiendo mucho q digamos
    //public static final String RESULTADO = "Sin resultado";

    // Helper para acceder al singleton ClienteAgregador desde las subclases
    public ClienteAgregador getClienteAgregador() {
        return ClienteAgregador.getInstance();
    }

    //Actualiza el Resultado
    public abstract void actualizarResultado();
}
