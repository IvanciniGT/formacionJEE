package com.curso.diccionarios.app.cliente.factorias;

import com.curso.diccionarios.api.SuministradorDeDiccionarios;
import com.curso.diccionarios.ficheros.SuministradorDeDiccionariosEnFicheros;

public class SuministradorDeDiccionariosFactory {

    public static SuministradorDeDiccionarios dameSuministradorDeDiccionarios(){
        // Los diccionarios son RECURSOS que viven en el classpath, dentro de una carpeta "diccionarios".
        // NO le damos una ruta de disco (un File), sino el nombre de la carpeta de recursos.
        // El suministrador los leerá como "streams" (getResourceAsStream), de forma que funcione igual:
        // - En el IDE y con "mvn exec:java" (los .class y los .txt están sueltos en target/classes)
        // - Y con "java -jar" (los .class y los .txt están comprimidos DENTRO del .jar)
        // Un File NO sabe leer dentro de un .jar; un stream sí.
        return new SuministradorDeDiccionariosEnFicheros("diccionarios");
    }
}
