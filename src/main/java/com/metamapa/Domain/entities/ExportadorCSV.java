package com.metamapa.Domain.entities;

import com.metamapa.Domain.dto.EstadisticasDTO;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

public class ExportadorCSV implements IExportador{
    @Override
    public String obtenerArchivoTipo(EstadisticasDTO estadisticas) {

        // Reservamos espacio donde escribir
        StringWriter writer = new StringWriter();

        // Escribe en ese espacio
        PrintWriter csv = new PrintWriter(writer);

        // Encabezado
        csv.println("Tipo de Estadistica,Clave,Subclave,Valor");
        csv.println();

        // Metadatos
        csv.println("Fecha de Actualizacion,,," +
            (estadisticas.getFechaActualizacion() != null ? estadisticas.getFechaActualizacion().toString() : "N/A"));
        csv.println();

        // 1. Hechos por provincia de colección
        csv.println("Hechos por Provincia de Coleccion");
        if (estadisticas.getHechosPorProvinciaDeColeccion() != null) {
            for (Map.Entry<Long, Map<String, Integer>> coleccion : estadisticas.getHechosPorProvinciaDeColeccion().entrySet()) {
                for (Map.Entry<String, Integer> provincia : coleccion.getValue().entrySet()) {
                    csv.println(String.format("Coleccion,%d,%s,%d",
                        coleccion.getKey(), provincia.getKey(), provincia.getValue()));
                }
            }
        }
        csv.println();

        // Provincia máxima por colección
        csv.println("Provincia con mas Hechos por Coleccion");
        if (estadisticas.getProvinciaMaximaPorColeccion() != null) {
            for (Map.Entry<Long, EstadisticasDTO.ProvinciaMaxima> entry : estadisticas.getProvinciaMaximaPorColeccion().entrySet()) {
                csv.println(String.format("Coleccion,%d,%s,%d",
                    entry.getKey(), entry.getValue().getProvincia(), entry.getValue().getCantidad()));
            }
        }
        csv.println();

        // 2. Hechos por categoría
        csv.println("Hechos por Categoria");
        if (estadisticas.getHechosPorCategoria() != null) {
            for (Map.Entry<String, Integer> entry : estadisticas.getHechosPorCategoria().entrySet()) {
                csv.println(String.format("Categoria,%s,,%d", entry.getKey(), entry.getValue()));
            }
        }
        csv.println();

        // Categoría máxima
        if (estadisticas.getCategoriaMaxima() != null) {
            csv.println("Categoria con mas Hechos");
            csv.println(String.format("Categoria Maxima,%s,,%d",
                estadisticas.getCategoriaMaxima().getCategoria(),
                estadisticas.getCategoriaMaxima().getCantidad()));
            csv.println();
        }

        // 3. Hechos por provincia por categoría
        csv.println("Hechos por Provincia segun Categoria");
        if (estadisticas.getHechosPorProvinciaPorCategoria() != null) {
            for (Map.Entry<String, Map<String, Integer>> categoria : estadisticas.getHechosPorProvinciaPorCategoria().entrySet()) {
                for (Map.Entry<String, Integer> provincia : categoria.getValue().entrySet()) {
                    csv.println(String.format("Categoria-Provincia,%s,%s,%d",
                        categoria.getKey(), provincia.getKey(), provincia.getValue()));
                }
            }
        }
        csv.println();

        // Provincia máxima por categoría
        csv.println("Provincia con mas Hechos por Categoria");
        if (estadisticas.getProvinciaMaximaPorCategoria() != null) {
            for (Map.Entry<String, EstadisticasDTO.ProvinciaMaxima> entry : estadisticas.getProvinciaMaximaPorCategoria().entrySet()) {
                csv.println(String.format("Categoria,%s,%s,%d",
                    entry.getKey(), entry.getValue().getProvincia(), entry.getValue().getCantidad()));
            }
        }
        csv.println();

        // 4. Hechos por hora por categoría
        csv.println("Hechos por Hora segun Categoria");
        if (estadisticas.getHechosPorHoraPorCategoria() != null) {
            for (Map.Entry<String, Map<Integer, Integer>> categoria : estadisticas.getHechosPorHoraPorCategoria().entrySet()) {
                for (Map.Entry<Integer, Integer> hora : categoria.getValue().entrySet()) {
                    csv.println(String.format("Categoria-Hora,%s,%d,%d",
                        categoria.getKey(), hora.getKey(), hora.getValue()));
                }
            }
        }
        csv.println();

        // Hora máxima por categoría
        csv.println("Hora con mas Hechos por Categoria");
        if (estadisticas.getHoraMaximaPorCategoria() != null) {
            for (Map.Entry<String, EstadisticasDTO.HoraMaxima> entry : estadisticas.getHoraMaximaPorCategoria().entrySet()) {
                csv.println(String.format("Categoria,%s,%d,%d",
                    entry.getKey(), entry.getValue().getHora(), entry.getValue().getCantidad()));
            }
        }
        csv.println();

        // 5. Solicitudes de eliminación spam
        csv.println("Solicitudes de Eliminacion");
        csv.println(String.format("Total de Solicitudes,,,%d",
            estadisticas.getTotalSolicitudesEliminacion() != null ? estadisticas.getTotalSolicitudesEliminacion() : 0));
        csv.println(String.format("Solicitudes Spam,,,%d",
            estadisticas.getSolicitudesSpam() != null ? estadisticas.getSolicitudesSpam() : 0));
        csv.println(String.format("Solicitudes No Spam,,,%d",
            estadisticas.getSolicitudesNoSpam() != null ? estadisticas.getSolicitudesNoSpam() : 0));
        csv.println(String.format("Porcentaje de Spam,,,%.2f%%",
            estadisticas.getPorcentajeSpam() != null ? estadisticas.getPorcentajeSpam() : 0.0));

        csv.flush();
        return writer.toString();
    }
}
