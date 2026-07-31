package com.curso.diccionarios.servicioweb;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

import com.google.gson.Gson;

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

    /**
     * Devuelve los códigos de los idiomas disponibles en el servidor,
     * llamando al endpoint GET /diccionarios que creamos en el día 7.
     *
     * ------------------------------------------------------------------
     * ESTE MÉTODO ES LA FACTURA DE UNA DECISIÓN QUE TOMASTEIS EL DÍA 7.
     * ------------------------------------------------------------------
     *
     * Aquel día añadisteis `dameIdiomas()` a la interfaz
     * SuministradorDeDiccionarios. Y al hacerlo os disteis cuenta de que
     * ROMPÍAIS la compilación de los tres módulos que ya la implementaban.
     * La solución fue declararlo como método `default` que lanza una
     * excepción, con este comentario:
     *
     *     "Esto no es para meter código... Esto es para asegurar la
     *      compatibilidad hacia atrás."
     *
     * Sólo lo implementó el suministrador de base de datos, que era el que lo
     * necesitaba. Este cliente HTTP se quedó SIN implementarlo y siguió
     * compilando y funcionando durante dos sesiones enteras.
     *
     * Hoy, la aplicación de escritorio necesita rellenar un desplegable con los
     * idiomas, así que ha llegado el momento de implementarlo aquí. Y fijaos en
     * lo que ha costado: añadir este método. Cero cambios en la interfaz, cero
     * cambios en los otros módulos, cero recompilaciones ajenas.
     *
     * Eso es el principio Abierto/Cerrado (la O de SOLID) funcionando de
     * verdad: el sistema se ha EXTENDIDO sin MODIFICAR nada de lo que ya
     * estaba bien. La decisión del día 7 se ha pagado sola nueve días después.
     */
    @Override
    public List<String> dameIdiomas(){
        String rutaCompleta = rutaServidor + "/diccionarios";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(rutaCompleta))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException(
                        "El servidor ha respondido " + response.statusCode()
                        + " al pedirle la lista de idiomas");
            }
            // El servidor manda un JSON del tipo: ["ES","EN","ELFICO"]
            // Gson lo convierte a un array de String, y de ahí a una List.
            String[] idiomas = new Gson().fromJson(response.body(), String[].class);
            return Arrays.asList(idiomas);
        } catch (Exception e) {
            throw new RuntimeException("Error al llamar al servicio web para obtener los idiomas disponibles", e);
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
