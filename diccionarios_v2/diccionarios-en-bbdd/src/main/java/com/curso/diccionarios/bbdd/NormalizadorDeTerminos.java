package com.curso.diccionarios.bbdd;

public class NormalizadorDeTerminos {
    public static String normalizar(String termino) {
        if (termino == null) {
            return null;
        }
        // Normalizamos el término a mayúsculas y eliminamos los espacios en blanco al inicio y al final
        return termino.trim().toUpperCase();
    }
}
