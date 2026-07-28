package com.curso.diccionarios.bbdd.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
// Esto me lo da Spring Data JPA, y me regala todas las operaciones más comunes de un repositorio, como guardar, borrar, buscar por id, etc.
import com.curso.diccionarios.bbdd.entidades.Idioma;

public interface IdiomaRepository extends JpaRepository<Idioma, Integer> {
    boolean existsByCodigo(String codigo); // Además de las operaciones que me da Spring Data JPA, 
    // puedo definir mis propias operaciones, 
    // como por ejemplo esta que me permite saber si existe un idioma con un código determinado.
    // Y MAGIA!
    // Esto es EL CÓDIGO QUE NECESITO PARA QUE SPRING DATA JPA ME GENERE LA IMPLEMENTACIÓN DE ESTA OPERACIÓN.
    // Spring va a escribir la query SQL por mi, y va a ejecutar la query en la BBDD, y me va a devolver el resultado.
}
