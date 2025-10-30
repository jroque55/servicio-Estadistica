package com.metamapa.Domain.entities;

public interface InterfaceEstadistica {
    String RESULTADO = "Sin resultado"; // constante pública

    void actualizarResultado(); // método abstracto

    // Devuelve el ClienteAgregador singleton disponible en la aplicación
    default ClienteAgregador getClienteAgregador() {
        return ClienteAgregador.getInstance();
    }
}
