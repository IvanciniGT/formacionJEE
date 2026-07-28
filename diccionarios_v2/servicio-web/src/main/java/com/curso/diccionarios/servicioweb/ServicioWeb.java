package com.curso.diccionarios.servicioweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@SpringBootApplication
// Con esta anotación le decimos a SpringBoot que esta clase es la clase principal de nuestra aplicación,
// y que debe encargarse de levantar el servidor web y poner a funcionar todos los componentes que tenga esta aplicación
// Pero qué componentes tiene la aplicación?
// Los componentes los va a buscar Spring en automático... En todos los ficheros que existan en esta misma carpeta o subcarpetas (paquete o subpaquetes)
// Como en nuestro caso, el componente que nos interesa es el SuministradorDeDiccionariosEnBBDD, que está en el paquete com.curso.diccionarios.bbdd, y este paquete no es un subpaquete de com.curso.diccionarios.servicioweb, Spring no lo va a encontrar automáticamente.
// Nos va a pasar similar con las entidades y los repositorios de la BBDD. Spring no los va a encontrar automáticamente, porque están en un paquete diferente al paquete principal de la aplicación.
// Necesitaos retocar esta anotación y añadir algunas anotaciones adicionales a esta clase.

@SpringBootApplication(scanBasePackages = {"com.curso.diccionarios"})
@EnableJpaRepositories(basePackages = {"com.curso.diccionarios.bbdd.repositorios"})
@EntityScan(basePackages = {"com.curso.diccionarios.bbdd.entidades"})

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
