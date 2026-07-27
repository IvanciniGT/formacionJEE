package com.curso.diccionarios.servicioweb;

import java.util.Optional;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;


import com.curso.diccionarios.api.Diccionario;
import com.curso.diccionarios.api.SuministradorDeDiccionarios;

public class SuministradorDeDiccionariosEnServicioWeb implements SuministradorDeDiccionarios {

    private final String rutaServidor;

    public SuministradorDeDiccionariosEnServicioWeb(String rutaServidor /*http://localhost:8080*/) {
        this.rutaServidor = rutaServidor;
    }

    public boolean tienesDiccionarioDe(String idioma){
        // Básicamente aquñi debo llamar al servidor por HTTP GET,
        // en el endpoint /diccionarios/{idioma} 
        // y ver si me devuelve un 200 OK o un 404 Not Found
        // Si devuelve un 200 OK, entonces retorno true
        // Si devuelve un 404 Not Found, entonces retorno false
        // Java tiene un API (razonablemente nuevo) para hacer llamadas HTTP, 
        // que se llama java.net.http.HttpClient
        // podemos usarlo fácilmente para hacer la llamada HTTP GET y ver el código de respuesta
        String rutaCompleta = rutaServidor + "/diccionarios/" + idioma;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(rutaCompleta))
                .GET()
                .build();
        // Hago la llamada a esa ruta.. Lanzo / ejecuto el request y obtengo la respuesta
        try {
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            // Ahora puedo ver el código de estado HTTP de la respuesta
            int statusCode = response.statusCode();
            if (statusCode == 200) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al llamar al servicio web para verificar diccionario de idioma: " + idioma, e);
        }
    }

    public Optional<Diccionario> dameDiccionarioDe(String idioma){
        boolean existe = tienesDiccionarioDe(idioma);
        if (!existe) {
            return Optional.empty();
        } else {
            return Optional.of(new DiccionarioEnServicioWeb(rutaServidor, idioma));
        }
    }
}
