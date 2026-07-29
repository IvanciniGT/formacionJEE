package com.curso.diccionarios.bbdd;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import com.curso.diccionarios.api.Diccionario;
import com.curso.diccionarios.bbdd.entidades.Palabra;
import com.curso.diccionarios.bbdd.entidades.Significado;
import com.curso.diccionarios.bbdd.repositorios.PalabraRepository;


public class DiccionarioEnBBDD implements Diccionario {

    private final String idioma;
    private final PalabraRepository palabraRepository;

    public DiccionarioEnBBDD(String idioma, PalabraRepository palabraRepository) { // Acabamos de configura lo que llamos ayer una INYECCION DE DEPENDENCIAS. En este caso, estamos inyectando el idioma y el repositorio de palabras en el constructor de la clase DiccionarioEnBBDD. Esto nos permite separar la lógica de negocio de la lógica de acceso a datos, y nos permite cambiar la implementación del repositorio sin tener que cambiar la lógica de negocio.
        this.idioma = idioma;
        this.palabraRepository = palabraRepository;
    }

    public String cualEsTuIdioma(){
        return idioma;
    }

    public boolean existe(String palabra){
        return palabraRepository.existsByPalabraAndIdioma_Codigo(NormalizadorDeTerminos.normalizar(palabra), idioma);
    }

    public Optional<List<String>> dameSignificados(String palabra){
        Optional<Palabra> palabraOptional = palabraRepository.findByPalabraAndIdioma_Codigo(NormalizadorDeTerminos.normalizar(palabra), idioma);
        if(palabraOptional.isPresent()){
            List<Significado> significados = palabraOptional.get().getSignificados();
            // Lo que debo devolver es un listado de Strings, no de Significados, por lo que debo transformar la lista de Significados en una lista de Strings. Para ello, voy a usar un stream y el método map para transformar cada Significado en su String correspondiente.
            List<String> significadosADevolver = new ArrayList<>();
            for(Significado significado : significados){
                significadosADevolver.add(significado.getSignificado());
            }
            return Optional.of(significadosADevolver);
        } else {
            return Optional.empty();
        }
    }
}
