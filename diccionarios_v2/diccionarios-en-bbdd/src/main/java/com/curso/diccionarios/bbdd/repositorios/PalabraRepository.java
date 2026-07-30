package com.curso.diccionarios.bbdd.repositorios;

import com.curso.diccionarios.bbdd.entidades.Palabra;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface PalabraRepository extends JpaRepository<Palabra, Integer> {
    
    // Saber si existe una palabra en un idioma determinado por su código de idioma y la palabra en sí. Esto es útil para evitar duplicados.
    boolean existsByPalabraAndIdioma_Codigo(String palabra, String codigo);
    // Al escribirlo así, con esta nomenclatura, Spring Data JPA entiende que estamos
    // buscando una palabra (palabra) en un idioma (idioma) a través de su código de idioma(codigo). 
    // Y me genera la query SQL correspondiente para buscar en la BBDD.
    Optional<Palabra> findByPalabraAndIdioma_Codigo(String palabra, String codigo);

    List<Palabra> findByIdioma_Codigo(String codigo);

}
