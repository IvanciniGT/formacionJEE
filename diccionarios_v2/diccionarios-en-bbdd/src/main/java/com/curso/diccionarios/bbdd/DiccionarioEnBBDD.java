package com.curso.diccionarios.bbdd;

import java.util.List;
import java.util.Optional;
import com.curso.diccionarios.api.Diccionario;

public class DiccionarioEnBBDD implements Diccionario {

    private final String idioma;

    public DiccionarioEnBBDD(String idioma) {
        this.idioma = idioma;
    }

    public String cualEsTuIdioma(){
        return idioma;
    }

    public boolean existe(String palabra){
        // TODO : Implementar la búsqueda de la palabra en la BBDD
    }

    public Optional<List<String>> dameSignificados(String palabra){
        // TODO : Implementar la búsqueda de los significados de la palabra en la BBDD
    }
}
