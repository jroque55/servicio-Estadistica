package com.metamapa.Domain.entities;

import com.metamapa.Domain.dto.EstadisticasDTO;

import java.io.PrintWriter;
import java.io.StringWriter;


public class ExportadorCSV implements IExportador{
    @Override
    public String obtenerArchivoTipo(EstadisticasDTO estadisticas) {

        //Reservamos espacio donde escribir
        StringWriter writer = new StringWriter();

        //Escribe en ese espacio
        PrintWriter csv = new PrintWriter(writer);
        csv.println("Estadistica, Clave, Resultado");


        return "";
    }
}
