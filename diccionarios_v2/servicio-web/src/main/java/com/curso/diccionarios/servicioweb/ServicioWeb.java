package com.curso.diccionarios.servicioweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// Con esta anotación le decimos a SpringBoot que esta clase es la clase principal de nuestra aplicación,
// y que debe encargarse de levantar el servidor web y poner a funcionar todos los componentes que tenga esta aplicación
// Pero qué componentes tiene la aplicación?
// Los componentes los va a buscar Spring en automático... En todos los ficheros que existan en esta misma carpeta o subcarpetas.
public class ServicioWeb {

    public static void main(String[] args) {
        // Como esta aplicación la estamos montando con SpriongBoot, 
        // Aquí irá una sola linea de código, que delega a SpringBoot la tarea de levantar el servidor web y exponer los servicios REST
        
        SpringApplication.run(ServicioWeb.class, args); // Esto es la INVERSION DE CONTROL
        // Traducido al Español: Spring! Arranca mi aplciación llamada ServicioWeb.
        // Y Spring se encarga.
        // Acabado!
    }
    
}
