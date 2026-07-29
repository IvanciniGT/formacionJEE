package com.curso.diccionarios.api;

import java.util.List;
import java.util.Optional;

public interface SuministradorDeDiccionarios {

    boolean tienesDiccionarioDe(String idioma);

    Optional<Diccionario> dameDiccionarioDe(String idioma);

    //List<String> idiomasDisponibles();
    // Escribir esta nueva funcion en la INTERFAZ implicaría que todas las clases
    // que implementan esta interfaz tendrían que implementar también esta nueva función.
    // Pero ahora mismo no lo están haciendo.
    // Si aplico este cambio, Hay 3 proyectos que dejan de compilar, porque no han implementado esta nueva función.
    // Para resolver esta situación, en JAVA 9 y posteriores, se puede usar la palabra reservada default para definir
    //  una implementación por defecto de la función en la interfaz.
    // Esto no es para meter código... Esto es para asegurar la compatibilidad hacia atrás. 
    default List<String> dameIdiomas() {
        throw new UnsupportedOperationException("Este método no está implementado en esta clase");
    }
    // Hay que tener mucho mucho cuidado al tocar una interfaz.
    // Ya que hago que las clases que implementan esta interfaz dejen de compilar, y eso es un problema.
    // Java como lenguaje ha ido evolucionando y ha introducido mecanismos como los métodos por defecto en las interfaces para
    // facilitar la evolución de las mismas sin romper la compatibilidad hacia atrás.

}
