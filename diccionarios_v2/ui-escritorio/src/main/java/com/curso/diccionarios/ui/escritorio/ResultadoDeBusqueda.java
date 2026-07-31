package com.curso.diccionarios.ui.escritorio;

import java.util.List;

/**
 * Modelo de la interfaz de usuario: el resultado de una búsqueda, ya masticado
 * y listo para pintar.
 *
 * ¿Por qué existe esta clase, si ya tenemos Diccionario y RespuestaPalabra?
 * Por la misma razón que en el día 8 decidimos mantener DOS clases
 * RespuestaPalabra (una en el servidor y otra en el cliente): cada capa tiene
 * derecho a su propio modelo. Aquí no queremos Optionals ni listas a null ni
 * llamadas que puedan tardar: queremos un objeto plano que responda a
 * "¿la encontraste?, ¿qué significa?, ¿qué le sugiero al usuario?".
 *
 * Es EXACTAMENTE la misma decisión que tomó la aplicación web de Angular con su
 * `ResultadoBusqueda` (ui-web/src/app/core/dominio/resultado-busqueda.ts).
 * Dos interfaces de usuario distintas, escritas en dos lenguajes distintos, por
 * dos herramientas distintas, y las dos llegan al mismo modelo. Cuando eso
 * pasa, suele ser señal de que el modelo es el correcto.
 *
 * `record` es una forma abreviada (Java 16+) de declarar una clase que sólo
 * transporta datos: genera constructor, getters, equals, hashCode y toString.
 * Es el POJO del día 4, pero sin las 40 líneas de código repetitivo.
 */
public record ResultadoDeBusqueda(
        String palabra,
        String idioma,
        boolean encontrada,
        List<String> significados,
        List<String> sugerencias) {

    public static ResultadoDeBusqueda encontrada(String palabra, String idioma,
                                                 List<String> significados) {
        return new ResultadoDeBusqueda(palabra, idioma, true, significados, List.of());
    }

    public static ResultadoDeBusqueda noEncontrada(String palabra, String idioma,
                                                   List<String> sugerencias) {
        // El servidor puede devolver null en el campo "similares". Lo
        // normalizamos AQUÍ, en la frontera, para que el código que pinta la
        // ventana no tenga que preguntarse nunca si una lista es null.
        return new ResultadoDeBusqueda(palabra, idioma, false, List.of(),
                sugerencias == null ? List.of() : sugerencias);
    }
}
