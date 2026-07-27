package com.curso.diccionarios.servicioweb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.curso.diccionarios.api.SuministradorDeDiccionarios;
import com.curso.diccionarios.ficheros.SuministradorDeDiccionariosEnFicheros;

@Configuration
// Con esta anotación le decimos a SpringBoot que esta clase 
// Contiene al menos una configuración de mi aplicación.
// En concreto voy a configurar aquí, qué suministrador de diccionarios concreto quiero que Spring cree y me inyecte en el constructor de la clase DiccionariosRestController
public class SuministradorDeDiccionariosConfiguration {

    @Bean // Esto es un tipo de configuración. 
    // Esta anotación le dice a Spring:
    // Esta función devuelve un objeto que es un componente de mi aplicación de tipo SuministradorDeDiccionarios.
    // Si alguien te pide un SuministradorDeDiccionarios, devuelve lo que devuelve esta función.
    public SuministradorDeDiccionarios dameSuministradorDeDiccionarios(){
        return new SuministradorDeDiccionariosEnFicheros("diccionarios");
    }
}
