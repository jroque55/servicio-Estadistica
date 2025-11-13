package com.metamapa.Domain.entities;

import com.metamapa.Domain.dto.EstadisticasDTO;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.Optional;


public class ExportadorCSV implements IExportador{

    private final EstadisticaCategoriaMaxima ecm;
    private final EstadisticaMaxHechosPorProvinciaDeUnaColeccion ehppc;
    private final EstadisticaHoraPorCategoria ehpc;
    private final EstadisticaProvinciaPorCategoria eppc;
    private final EstadisticaSpamEliminacion ese;

    public ExportadorCSV(Long idColeccion, String categoria) {
        this.ecm = new EstadisticaCategoriaMaxima();
        this.ehppc = new EstadisticaMaxHechosPorProvinciaDeUnaColeccion(idColeccion);
        this.ehpc = new EstadisticaHoraPorCategoria(categoria);
        this.eppc = new EstadisticaProvinciaPorCategoria(categoria);
        this.ese = new EstadisticaSpamEliminacion();
    }
    /*
    @Override
    public String obtenerArchivoTipo(EstadisticasDTO estadisticas) {
        // Crear un espacio para escribir el CSV
        StringWriter writer = new StringWriter();
        PrintWriter csv = new PrintWriter(writer);

        // Encabezado
        csv.println("Estadistica, Clave, Resultado");

        // Actualizamos todas las estadísticas antes de exportar
        ecm.actualizarResultado();
        ehppc.actualizarResultado();
        ehpc.actualizarResultado();
        eppc.actualizarResultado();
        ese.actualizarResultado();

        // 📊 1. Categoría con más hechos
        Optional<Map.Entry<String, Integer>> catMax = ecm.getCategoriaConMasHechos();
        catMax.ifPresent(entry ->
                csv.printf("Categoría más frecuente,%s,%d%n", entry.getKey(), entry.getValue())
        );

        // 📍 2. Provincia con más hechos en la colección
        Optional<Map.Entry<String, Integer>> provMax = ehppc.getProvinciaConMasHechos();
        provMax.ifPresent(entry ->
                csv.printf("Provincia con más hechos (colección %d),%s,%d%n",
                        estadisticas.getIdColeccion(), entry.getKey(), entry.getValue())
        );

        // 3. Conteo de hechos por hora (categoría)
        csv.printf("Horas por categoría (%s),%s,%s%n",
                ehpc.getCategoria(), "-", ehpc.getHoraConteo().toString());

        // 4. Conteo de hechos por provincia (categoría)
        csv.printf("Provincias por categoría (%s),%s,%s%n",
                eppc.getCategoria(), "-", eppc.getProvinciaConteo().toString());

        // 5. Elemento con más solicitudes de eliminación de spam
        Optional<Map.Entry<String, Integer>> spamMax = ese.getElementoConMasSpam();
        spamMax.ifPresent(entry ->
                csv.printf("Elemento con más solicitudes de eliminación,%s,%d%n",
                        entry.getKey(), entry.getValue())
        );

        csv.flush();
        return writer.toString();
    }*/
}
