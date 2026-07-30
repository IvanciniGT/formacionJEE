package com.curso.diccionarios.bbdd;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.curso.diccionarios.api.Diccionario;
import com.curso.diccionarios.api.SuministradorDeDiccionarios;
import com.curso.diccionarios.bbdd.entidades.Idioma;
import com.curso.diccionarios.bbdd.repositorios.IdiomaRepository;
import com.curso.diccionarios.bbdd.repositorios.PalabraRepository;

import java.util.ArrayList;
import java.util.List;


// CAPA DAO = Data Access Object
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
        return idiomasRepository.existsByCodigo(NormalizadorDeTerminos.normalizar(idioma));
    }

    public Optional<Diccionario> dameDiccionarioDe(String idioma){
        boolean existe = idiomasRepository.existsByCodigo(NormalizadorDeTerminos.normalizar(idioma));
        if(!existe){
            return Optional.empty();
        } else {
            Diccionario diccionario = new DiccionarioEnBBDD(NormalizadorDeTerminos.normalizar(idioma), palabraRepository);
            return Optional.of(diccionario);
        }
    }

    public List<String> dameIdiomas() {
        List<Idioma> idiomas = idiomasRepository.findAll();
        List<String> listadoCodigos = new ArrayList<String>();
        for(Idioma idioma : idiomas){
            listadoCodigos.add(idioma.getCodigo());
        }
        return listadoCodigos;
    }
}
