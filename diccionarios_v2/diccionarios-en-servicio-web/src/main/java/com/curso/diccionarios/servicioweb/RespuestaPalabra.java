package com.curso.diccionarios.servicioweb;

import java.util.List;

// POJO: Plain Old Java Object
// Esta clase es un POJO. Es decir, es una clase que solo tiene datos
// Pero no tiene lógica.
public class RespuestaPalabra {
    private String palabra;
    private String idioma;
    private List<String> significados;
    private List<String> similares;

    public RespuestaPalabra(String palabra, String idioma, List<String> significados) {
        this.palabra = palabra;
        this.idioma = idioma;
        this.significados = significados;
    }
    public RespuestaPalabra(String idioma) {
        this.idioma = idioma;
    }
    public RespuestaPalabra(String idioma, List<String> similares) {
        this.idioma = idioma;
        this.similares = similares;
    }
    public RespuestaPalabra() {
    }

    public String getPalabra() {
        return palabra;
    }

    public String getIdioma() {
        return idioma;
    }

    public List<String> getSignificados() {
        return significados;
    }

    public List<String> getSimilares() {
        return similares;
    }
}
