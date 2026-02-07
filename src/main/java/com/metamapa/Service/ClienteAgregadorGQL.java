package com.metamapa.Service;

import com.metamapa.Domain.dto.input.*;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class ClienteAgregadorGQL {

    private final HttpGraphQlClient graphQlClient;

    private static volatile ClienteAgregadorGQL INSTANCE;

    public ClienteAgregadorGQL (WebClient.Builder builder) {
        WebClient webClient = builder.baseUrl("https://agregador-79iw.onrender.com/graphql").build();
        this.graphQlClient = HttpGraphQlClient.builder(webClient).build();
        INSTANCE = this;
    }

    public static ClienteAgregadorGQL getInstance() {
        return INSTANCE;
    }

    public List<ProvCatDTO> obtenerCantHechosPorProvinciaSegun(String categoria) {
        String query = """
                query($cat: String){
                obtenerCantidadDeHechoXProvinciaXCategoria (categoria: $cat){
                    provincia
                    cantidad
                }
                """;
        return graphQlClient.document(query)
                .variable("cat", categoria)
                .retrieve("obtenerCantidadDeHechoXProvinciaXCategoria")
                .toEntityList(ProvCatDTO.class)
                .block();
    }

    public List<CatHourDTO> obtenerHechosPorHoraSegun(String categoria) {
        String query = """
                query($cat: String){
                obtenerCantidadDeHechosXHoraXCategoria (categoria: $cat){
                    hora
                    cantidad
                }
                """;
        return graphQlClient.document(query)
                .variable("cat", categoria)
                .retrieve("obtenerCantidadDeHechosXHoraXCategoria")
                .toEntityList(CatHourDTO.class)
                .block();
    }

    public List<ProvinceDTO> obtenerCantHechosXProvinciaDe(String coleccion) {
        String query = """
                query($col: String){
                obtenerCantidadDeHechosXProvincia (coleccion: $col){
                    provincia
                    cantidad
                }
                """;
        return graphQlClient.document(query)
                .variable("col", coleccion)
                .retrieve("obtenerCantidadDeHechosXProvincia")
                .toEntityList(ProvinceDTO.class)
                .block();
    }

    public List<CategoryDTO> obtenerCantHechosPorCategoria(){
        String query = """
                query{
                obtenerCantidadDeHechosXCategoria {
                    categoria
                    cantidad
                }
                """;
        return graphQlClient.document(query)
                .retrieve("obtenerCantidadDeHechosXCategoria")
                .toEntityList(CategoryDTO.class)
                .block();
    }

    public SpamSummaryDTO obtenerDatosSolicitudesSpam(){
        String query = """
                query{
                obtenerCantidadSpamEnSolicitudes {
                    totalSolicitudes
                    totalSpam
                }
                """;
        return graphQlClient.document(query)
                .retrieve("obtenerCantidadSpamEnSolicitudes")
                .toEntity(SpamSummaryDTO.class)
                .block();
    }

    public List<String> obtenerColecciones() {
        String query = "query { obtenerColecciones }";

        return graphQlClient.document(query)
                .retrieve("obtenerColecciones")
                .toEntityList(String.class)
                .block();
    }

    public List<String> obtenerCategorias() {
        String query = "query { obtenerCategorias }";

        return graphQlClient.document(query)
                .retrieve("obtenerCategorias")
                .toEntityList(String.class)
                .block();
    }

    public List<String> obtenerProvincias() {
        String query = "query { obtenerProvincias }";

        return graphQlClient.document(query)
                .retrieve("obtenerProvincias")
                .toEntityList(String.class)
                .block();
    }
}
