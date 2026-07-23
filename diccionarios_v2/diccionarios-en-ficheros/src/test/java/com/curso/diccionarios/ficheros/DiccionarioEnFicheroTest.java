package com.curso.diccionarios.ficheros;

// Este fichero será ejecutado por JUNIT
// Y al ejecutarlo, va a buscar TODAS las funciones
// que tengan la anotación @Test
// Y las va a ejecutar una a una.
// Cada una puede acabar de 3 formas posibles:
// Sin problemas        ok
// Con problemas        failure
// Generando un error   error

// Cuando JUnit acabe de ejecutar todas las pruebas, 
// Generará un informe indicando:
// - El total de pruebas ejecutadas
// - Cuantas han acabado bien
// - Cuantas han acabado mal (failure)
// - Cuantas han generado un error (error)
// Y de las que no hayan ido bien, 
// Nos dará su nombre y el motivo del fallo o del error.

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import com.curso.diccionarios.api.Diccionario;
import com.curso.diccionarios.ficheros.DiccionarioEnFichero;

public class DiccionarioEnFicheroTest {

    // Para una clase, definimos muchas pruebas
    // Al menos 1 por cada función que tenga la clase y que quiera probar.
    @Test
    @DisplayName("Obtener el idioma de un diccionario")
    void cualEsTuIdiomaTest() {
        // Todo programa/función de pruebas tiene 3 partes:
        // 1.Contexto
        // Dado que tengo un DiccionarioEnFichero 
        // Para idioma "es", 
        DiccionarioEnFichero diccionario = new DiccionarioEnFichero("es", null);
        // 2.Acción a Probar
        // cuando le pido su idioma,
        String idiomaDevuelto = diccionario.cualEsTuIdioma();
        // 3.Verificación (Aserciones)
        // Entonces me devuelve "es"
        // Las comprobaciones las hacemos a través de una clase que ofrece JUnit, llamada Assertions
        Assertions.assertEquals("es", idiomaDevuelto);
    }

    @Test
    @DisplayName("Verificar que existe una palabra que sí está en el diccionario")
    void verificarQueExisteUnaPalabraQueSiEstaEnElDiccionarioTest() {
        Map<String, List<String>> tablaDeMentirijillaDePalabrasYSignificados = new HashMap<>();
        tablaDeMentirijillaDePalabrasYSignificados.put("melón", List.of("Fruto de la planta del melón, de forma redonda u ovalada.", "Persona con pocas luces. Eres un melón!"));
        // Dado que tengo un DiccionarioEnFichero 
        // Para idioma "es", 
        DiccionarioEnFichero diccionario = new DiccionarioEnFichero("es", tablaDeMentirijillaDePalabrasYSignificados);
        // cuando le pregunto si existe la palabra "melón"
        boolean existe = diccionario.existe("melón");
        // Entonces me devuelve true
        Assertions.assertTrue(existe);
    }


    @Test
    @DisplayName("Verificar que NO existe una palabra que NO está en el diccionario")
    void verificarQueNoExisteUnaPalabraQueNoEstaEnElDiccionarioTest() {
        // Contexto
        Map<String, List<String>> tablaDeMentirijillaDePalabrasYSignificados = new HashMap<>();
        tablaDeMentirijillaDePalabrasYSignificados.put("melón", List.of("Fruto de la planta del melón, de forma redonda u ovalada.", "Persona con pocas luces. Eres un melón!"));
        DiccionarioEnFichero diccionario = new DiccionarioEnFichero("es", tablaDeMentirijillaDePalabrasYSignificados);
        // Acción a Probar
        boolean existe = diccionario.existe("chorizo");
        // Verifico que devuelve lo que debe devolver.
        Assertions.assertFalse(existe);
    }    

    @Test
    @DisplayName("No obtengo significados de una palabra que NO existe en el diccionario")
    void obtenerSignificadosDePalabraQueNOExisteTest() {
        // Contexto
        Map<String, List<String>> tablaDeMentirijillaDePalabrasYSignificados = new HashMap<>();
        tablaDeMentirijillaDePalabrasYSignificados.put("melón", List.of("Fruto de la planta del melón, de forma redonda u ovalada.", "Persona con pocas luces. Eres un melón!"));
        DiccionarioEnFichero diccionario = new DiccionarioEnFichero("es", tablaDeMentirijillaDePalabrasYSignificados);
        // Acción a Probar
        Optional<List<String>> significados = diccionario.dameSignificados("chorizo");
        // Verifico que devuelve lo que debe devolver.
        Assertions.assertTrue(significados.isEmpty());
    }
    @Test
    @DisplayName("Si obtengo los significados de una palabra que SÍ existe en el diccionario")
    void obtenerSignificadosDePalabraQueSiExisteTest() {
        // Contexto
        Map<String, List<String>> tablaDeMentirijillaDePalabrasYSignificados = new HashMap<>();
        tablaDeMentirijillaDePalabrasYSignificados.put("melón", List.of("Fruto de la planta del melón, de forma redonda u ovalada.", "Persona con pocas luces. Eres un melón!"));
        DiccionarioEnFichero diccionario = new DiccionarioEnFichero("es", tablaDeMentirijillaDePalabrasYSignificados);
        // Acción a Probar
        Optional<List<String>> significados = diccionario.dameSignificados("melón");
        // Verifico que devuelve lo que debe devolver.
        Assertions.assertTrue(significados.isPresent());
        List<String> significadosDevueltos = significados.get();
        Assertions.assertEquals(2, significadosDevueltos.size());
        // Y el primero debe ser:
        Assertions.assertEquals("Fruto de la planta del melón, de forma redonda u ovalada.", significadosDevueltos.get(0));
        // Y el segundo debe ser:
        Assertions.assertEquals("Persona con pocas luces. Eres un melón!", significadosDevueltos.get(1));
    }
}
