package com.metamapa.Domain.entities;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.metamapa.Domain.dto.output.EstadisticaOutputDTO;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class ExportadorCSV implements IExportador{

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String exportar(EstadisticaOutputDTO obj) {
        try {
            Class<?> clazz = obj.getClass();
            Field[] fields = clazz.getDeclaredFields();

            StringBuilder sb = new StringBuilder();

            // HEADER
            for (int i = 0; i < fields.length; i++) {
                sb.append(fields[i].getName());
                if (i < fields.length - 1) sb.append(",");
            }
            sb.append("\n");

            // VALUES
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                Object value = fields[i].get(obj);

                if (value == null) {
                    sb.append("");
                } else if (isSimpleValue(value)) {
                    sb.append(value.toString());
                } else {
                    // Serialización para listas u objetos complejos
                    sb.append("\"").append(mapper.writeValueAsString(value)).append("\"");
                }

                if (i < fields.length - 1) sb.append(",");
            }

            return sb.toString();

        } catch (Exception e) {
            throw new RuntimeException("Error exportando CSV", e);
        }
    }

    private boolean isSimpleValue(Object value) {
        return value instanceof String ||
                value instanceof Integer ||
                value instanceof Long ||
                value instanceof Boolean;
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
