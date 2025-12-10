package com.metamapa.Domain.entities;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.metamapa.Domain.dto.input.CatHourDTO;
import com.metamapa.Domain.dto.input.CategoryDTO;
import com.metamapa.Domain.dto.input.ProvCatDTO;
import com.metamapa.Domain.dto.input.ProvinceDTO;
import com.metamapa.Domain.dto.output.EstadisticaOutputDTO;
import org.springframework.stereotype.Component;
import com.metamapa.Domain.entities.*;
import java.lang.reflect.Field;
import com.metamapa.Domain.entities.*;

import static com.metamapa.Domain.entities.EnumTipoEstadistica.CANTSOLICITUDESSPAM;

@Component
public class ExportadorCSV implements IExportador{

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String exportar(InterfaceEstadistica estadistica) {

        if (estadistica instanceof EstadisticaMaxHechosPorProvinciaDeUnaColeccion e1) {
            return this.exportarProvincias(e1);
        }

        if (estadistica instanceof EstadisticaCategoriaMaxima e2) {
            return this.exportarMaxCategoria(e2);
        }

        if (estadistica instanceof EstadisticaHoraPorCategoria e3) {
            return this.exportarMaxHoraCategoria(e3);
        }

        if (estadistica instanceof EstadisticaProvinciaPorCategoria e4) {
            return this.exportarMaxProvinciaXCategoria(e4);
        }

        if (estadistica instanceof EstadisticaSpamEliminacion e5) {
            return this.exportarSpam(e5);
        }

        throw new IllegalArgumentException("Tipo de estadística no soportado: " + estadistica.getClass());
    }



    public String exportarProvincias(EstadisticaMaxHechosPorProvinciaDeUnaColeccion est) {
        StringBuilder sb = new StringBuilder();
        sb.append("#").append(est.getClass().getSimpleName()+"\n");
        sb.append("Coleccion ,Provincia,Cantidad_Hechos\n");
        if (est.getProvincias() != null) {
            for (ProvinceDTO p : est.getProvincias()) {
                sb.append(est.getDiscriminante().getValor()).append(",").append(p.getProvincia()).append(",")
                        .append(p.getCantidad()).append("\n");
            }
        }
        return sb.toString();
    }

    public String exportarMaxCategoria(EstadisticaCategoriaMaxima est) {
        StringBuilder sb = new StringBuilder();
        sb.append("#").append(est.getClass().getSimpleName()+"\n");
        sb.append("Categoria,Cantidad_Hechos\n");

        for (CategoryDTO p : est.getCategorias()) {
            sb.append(p.getCategoria()).append(",")
                    .append(p.getCantidadHechos()).append("\n");
        }
        return sb.toString();
    }

    private String exportarMaxHoraCategoria(EstadisticaHoraPorCategoria est) {
        StringBuilder sb = new StringBuilder();
        sb.append("#").append(est.getClass().getSimpleName()+"\n");
        sb.append("Categoria,Hora ,Cantidad_hechos\n");
        if(est.getCantidadXHoras() != null) {
            for (CatHourDTO p : est.getCantidadXHoras()) {
                sb.append(est.getDiscriminante().getValor()).append(",").append(p.getHora()).append(",")
                        .append(p.getCantidad()).append("\n");
            }
        }
        return sb.toString();
    }

    private String exportarMaxProvinciaXCategoria(EstadisticaProvinciaPorCategoria est) {
        StringBuilder sb = new StringBuilder();
        sb.append("#").append(est.getClass().getSimpleName()+"\n");
        sb.append("Categoria,Cantidad_Hechos\n");
        if(est.getProvincias() != null) {
            for (ProvCatDTO p : est.getProvincias()) {
                sb.append(est.getDiscriminante().getValor()).append(",").append(p.getProvincia()).append(",")
                        .append(p.getCantidad()).append("\n");
            }
        }
        return sb.toString();
    }

    private String exportarSpam(EstadisticaSpamEliminacion est) {
        StringBuilder sb = new StringBuilder();
        sb.append("#").append(est.getClass().getSimpleName()+"\n");
        sb.append("Cantidad_Solicitudes,Cantidad_Spam\n");

        sb.append(est.getTotalDeSolicitudes()).append(",").append(est.getResultado()).append("\n");

        return sb.toString();
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
