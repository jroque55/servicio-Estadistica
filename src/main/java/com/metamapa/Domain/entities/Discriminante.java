package com.metamapa.Domain.entities;

import lombok.Data;

import java.net.DatagramSocket;

@Data
public class Discriminante {
    private EnumTipoDiscriminante tipoDiscriminante;
    private String valor;

    public Discriminante() {}
    public Discriminante(EnumTipoDiscriminante tipoDiscriminante, String valor) {
        this.tipoDiscriminante = tipoDiscriminante;
        this.valor = valor;
    }
}
