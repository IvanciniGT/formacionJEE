package com.curso.diccionarios.bbdd;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.curso.diccionarios.api.Diccionario;
import com.curso.diccionarios.api.SuministradorDeDiccionarios;
import com.curso.diccionarios.bbdd.repositorios.IdiomaRepository;
import com.curso.diccionarios.bbdd.repositorios.PalabraRepository;

@Component
// Esta anotación hace que si alguien pide un SuministradorDeDiccionarios,
// SpringBoot le devuelva un objeto de esta clase.
// Spring es una herramienta pensada para ayudarnos a usar el patrón de diseño Inyección de Dependencias.
public class SuministradorDeDiccionariosEnBBDD implements SuministradorDeDiccionarios {

    private final IdiomaRepository idiomasRepository;
    private final PalabraRepository palabraRepository;

    public SuministradorDeDiccionariosEnBBDD(IdiomaRepository idiomasRepository, PalabraRepository palabraRepository) { // INYECCION DE DEPDNENCIAS
        this.idiomasRepository = idiomasRepository;
        this.palabraRepository = palabraRepository;
    }

    public boolean tienesDiccionarioDe(String idioma){
        return idiomasRepository.existsByCodigo(idioma); // Este método devuelve true si existe un idioma en la BBDD con el código de idioma especificado. Esto es útil para saber si podemos crear un diccionario para ese idioma.
    }

    public Optional<Diccionario> dameDiccionarioDe(String idioma){
        boolean existe = idiomasRepository.existsByCodigo(idioma);
        if(!existe){
            return Optional.empty();
        } else {
            Diccionario diccionario = new DiccionarioEnBBDD(idioma, palabraRepository);
            return Optional.of(diccionario);
        }
    }
    
}
