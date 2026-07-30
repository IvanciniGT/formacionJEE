package com.curso.diccionarios.bbdd;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

import com.curso.diccionarios.api.Diccionario;
import com.curso.diccionarios.api.DistanciaLevensthein;
import com.curso.diccionarios.bbdd.entidades.Palabra;
import com.curso.diccionarios.bbdd.entidades.Significado;
import com.curso.diccionarios.bbdd.repositorios.PalabraRepository;


public class DiccionarioEnBBDD implements Diccionario {

    public static final int DISTANCIA_MAXIMA_ADMISIBLE = 2; // Definimos una constante para la distancia máxima admisible entre palabras. Esto nos permite cambiar el valor de la distancia máxima admisible en un solo lugar, y nos permite entender mejor el código, ya que el nombre de la constante es más descriptivo que un número mágico.

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


    public List<String> palabrasSimilares(String palabraObjetivo) {

        List<Palabra> palabrasDelDiccionario = palabraRepository.findByIdioma_Codigo(idioma);
        return palabrasDelDiccionario.stream()
                .map(      palabra         -> palabra.getPalabra())
                .filter(   palabra         -> Math.abs(palabra.length() - palabraObjetivo.length()) <= DISTANCIA_MAXIMA_ADMISIBLE                                )
                .map(      palabra         -> new PalabraPuntuada(palabra, DistanciaLevensthein.distance(palabraObjetivo, palabra))      )
                .filter(   palabraPuntuada -> palabraPuntuada.getDistancia() <= DISTANCIA_MAXIMA_ADMISIBLE                                                        )
                .sorted(   Comparator.comparingInt(PalabraPuntuada::getDistancia)                                                       )
                .map(      palabraPuntuada -> palabraPuntuada.getPalabra()                                                              )
                .limit(10)
                .collect(Collectors.toList());
    }

    private static class PalabraPuntuada {
        private String palabra;
        private int distancia;

        public PalabraPuntuada(String palabra, int distancia) {
            this.palabra = palabra;
            this.distancia = distancia;
        }

        public String getPalabra() {
            return palabra;
        }

        public int getDistancia() {
            return distancia;
        }
    }

}
