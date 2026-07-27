package com.curso.diccionarios.servicioweb;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import com.curso.diccionarios.api.Diccionario;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import com.google.gson.Gson; // Esta librería la necesito añadir como dependencia al pom.xml
// Es una librería gratuita de google que sirve para convertir objetos Java a JSON y JSON a objetos Java

public class DiccionarioEnServicioWeb implements Diccionario {

    private final String idioma;
    private final String rutaServidor;

    public DiccionarioEnServicioWeb(String rutaServidor /*http://localhost:8080*/, String idioma) {
        this.idioma = idioma;
        this.rutaServidor = rutaServidor;
    }

    public String cualEsTuIdioma(){
        return idioma;
    }

    public boolean existe(String palabra){
        Optional<RespuestaPalabra> respuesta = llamarAlServicioWeb(palabra);
        if (respuesta.isPresent()) {
            RespuestaPalabra resp = respuesta.get();
            return resp.getSignificados() != null; 
        } else {
            return false;
        }
    }

    public Optional<List<String>> dameSignificados(String palabra){
        Optional<RespuestaPalabra> respuesta = llamarAlServicioWeb(palabra);
        if (respuesta.isPresent()) {
            RespuestaPalabra resp = respuesta.get();
            return Optional.ofNullable(resp.getSignificados());
        } else {
            return Optional.empty();
        }
    }
    // En ambos casos (las 2 funciones anteriores) debo hacer la misma llamada HTTP GET al endpoint /diccionarios/{idioma}/{palabra}
    // Y mirar el código de estado HTTP de la respuesta
    // Si es 200 OK, entonces en el caso de existe() retorno true, y en el caso de dameSignificados() retorno un Optional con la lista de significados (que viene en el cuerpo de la respuesta HTTP)
    // Si es 404 Not Found, entonces en el caso de existe() retorno false, y en el caso de dameSignificados() retorno un Optional vacío.

    private Optional<RespuestaPalabra> llamarAlServicioWeb(String palabra) {
        // Hacer la llamada HTTP GET al endpoint /diccionarios/{idioma}/{palabra}
        // y devolver un objeto RespuestaPalabra con los datos de la respuesta
        // o lanzar una excepción si hay algún error
        String rutaCompleta = rutaServidor + "/diccionarios/" + idioma + "/" + palabra;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(rutaCompleta))
                .GET()
                .build();
        try {
            // Lo que quiero es devolver la respuesta del servicio web como un objeto RespuestaPalabra
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // La respuesta es un JSON que representa un objeto RespuestaPalabra    
            String body = response.body();
            RespuestaPalabra respuesta = new Gson().fromJson(body, RespuestaPalabra.class);
            return Optional.of(respuesta);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
