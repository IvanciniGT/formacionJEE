package com.curso.diccionarios.servicioweb;
/*
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.curso.diccionarios.api.SuministradorDeDiccionarios;
import com.curso.diccionarios.ficheros.SuministradorDeDiccionariosEnFicheros;
*/
//@Configuration
// Con esta anotación le decimos a SpringBoot que esta clase 
// Contiene al menos una configuración de mi aplicación.
// En concreto voy a configurar aquí, qué suministrador de diccionarios concreto quiero que Spring cree y me inyecte en el constructor de la clase DiccionariosRestController
public class SuministradorDeDiccionariosConfiguration {

//    @Bean // Esto es un tipo de configuración. 
    // Esta anotación le dice a Spring:
    // Esta función devuelve un objeto que es un componente de mi aplicación de tipo SuministradorDeDiccionarios.
    // Si alguien te pide un SuministradorDeDiccionarios, devuelve lo que devuelve esta función.
//    public SuministradorDeDiccionarios dameSuministradorDeDiccionarios(){
//        return new SuministradorDeDiccionariosEnFicheros("diccionarios");
        // Ahora que tenemos un suministrador de diccionarios en BBDD
        // Lo suyo sería que aquí devolviesemos un SuministradorDeDiccionariosEnBBDD.
        // Podríamos hacerlo de la misma forma que lo hicimos con el suministrador de diccionarios en ficheros.
        // Realmente Spring me da una forma más sencilla de hacer este trabajo.
        // Lo que vamos a hacer es eliminar esta clase.
        // En el curso, la voy a dejar.. para no romper el ejemplo de la inyección de dependencias.
        // Pero lo voy a dejar todo comentado, para que veas cómo se hacía antes de SpringBoot.
        // en lugar de crear este fichero, lo único que voy a hacer es 
        // En el fichero del SuministradorDeDiccionariosEnBBDD, le voy a poner la anotación @Component antes del nomnbre de la clase.
        // Al hacerlo, SpringBoot se encargará de crear un objeto de esa clase 
        // y de inyectarlo a quién lo pida. En este caso, a la clase DiccionariosRestController.
        // Ahora toca cambiar las dependencias entre proyectos. 
        // En el proyecto de servicio web, 
        // vamos a eliminar la dependencia del proyecto diccionarios-en-ficheros 
        // y vamos a añadir la dependencia del proyecto diccionarios-en-bbdd.
//    }
}
