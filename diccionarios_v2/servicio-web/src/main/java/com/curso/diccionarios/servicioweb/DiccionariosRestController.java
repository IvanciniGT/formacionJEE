package com.curso.diccionarios.servicioweb;
/* 
En esta clase vamos a definir las rutas, LOS ENDPOINTS que tendrá nuestro servicio REST.
    GET + /diccionarios/<idioma>
      En nuestra máquina será del tipo:           http://localhost:8080/diccionarios/es
      En el servidor de producción será del tipo: https://diccionarios.miempresa.com/diccionarios/es
        En este caso, el servidor NO VA A DEVOLVER NINGUN JSON en la respuesta http (BODY).
        Vamos a jugar solo con los estados HTTP (STATUS CODE) de la respuesta http:
        - 200 OK: El diccionario existe
        - 404 Not Found: El diccionario no existe
        - 500 Internal Server Error: El servidor no puede atender la petición (por ejemplo, porque la BBDD donde están los diccionarios no está disponible)


    GET + /diccionarios/<idioma>/<palabra>
      En nuestra máquina será del tipo:           http://localhost:8080/diccionarios/es/melón
      En el servidor de producción será del tipo: https://diccionarios.miempresa.com/diccionarios/es/melón

      En este caso, hay varias cosas que el servidor puede devolver en la respuesta http (BODY):
        - 200 OK: El diccionario y la palabra existen
            BODY -> JSON -> {"diccionario": "es", "palabra": "melón", "significados": ["fruta...", "color..."]}
        - 404 Not Found: El diccionario o la palabra no existen
            BODY -> JSON -> {"diccionario": "es"}   Si la palabra no existe, pero el diccionario sí.
            BODY -> JSON -> {}                      Si el diccionario no existe
        - 500 Internal Server Error: El servidor no puede atender la petición (por ejemplo, porque la BBDD donde están los diccionarios no está disponible)
           BODY -> JSON -> {}

Para que Spring entienda que este fichero define un COMPONENTE 
con RUTAS HTTP que deben configurarse en el servidor de aplciaciones,
necesitamos escribir una anotación antes del nombre de la clase:
@RestController
 */
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;


import com.curso.diccionarios.api.Diccionario;
import com.curso.diccionarios.api.SuministradorDeDiccionarios;

import com.curso.diccionarios.servicioweb.modelos.RespuestaPalabra;


@RestController 
// Esta clase define un COMPONENTE de la aplicación, que define ENDPOINTS HTTP.
// Solo con esta anotación Spring automáticamente DETECTARA ESTA CLASE
// Configurará las rutas aquí definidas en el servidor de aplicaciones web (TOMCAT)
// Y se encargará de transformar de JSON a JAVA y de JAVA a JSON automáticamente
// Los datos que mande el cliente al servidor y los que mande el servidor al cliente
public class DiccionariosRestController {

    private final SuministradorDeDiccionarios suministradorDeDiccionarios; // Necesito un suministrador de diccionarios para poder atender las peticiones HTTP que lleguen a este servicio web REST

    // En el constructor de la clase, pongo como argumento un suministrador de diccionarios
    // Y lo guardó en una variable interna, mia.
    // Yo, al crear esta clase (estoy aplicando el principio: SoC: Separation of Concerns), 
    // no me voy a preocupar de crear un suministrador de diccionarios.
    // Tampoco me preocupo de dónde sale el suministrador de diccionarios. 
    // Solo me preocupo de explicitar en el constructor que PRECISO de un suministrador de diccionarios para poder funcionar.
    // Spring, que es quien va a generar una instancia de esta clase, 
    // Se encargará de crear un suministrador de diccionarios y pasarlo como argumento al constructor de esta clase.
    // Esto es lo que llamamos una INYECCION DE DEPENDENCIAS. (Dependency Injection)
    // LA pregunta es qué va a entregar Spring? Qué suministrador concreto va a entregar.
    // Lo tengo que configurar... en algo parecido a la factoría que teníamos en la aplicación de consola.
    public DiccionariosRestController(SuministradorDeDiccionarios suministradorDeDiccionarios) { // Inyección de dependencias: Spring se encarga de crear un suministrador de diccionarios y pasarlo como argumento al constructor de esta clase
        this.suministradorDeDiccionarios = suministradorDeDiccionarios;
    }

    @GetMapping("/diccionarios/test") // Voy a declarar la ruta /diccionarios/test que atiende el verbo HTTP GET
    // Estas fun ciones vamos a hacer que devuelvan un objeto, Ese objeto lo define Springboot y permite establecer el código de estado HTTP de la respuesta, y el BODY de la respuesta (que puede ser un JSON)
    // El tipo de objeto se llaman ResponseEntity, y es un objeto que define SpringBoot.
    // El ResponseEntity<T>, se define con un tipo de dato T,. que es el tipo de dato que va a ir en el BODY de la respuesta HTTP.
    // Si no queremos que vaya ningún dato en el BODY de la respuesta HTTP, podemos usar ResponseEntity<Void> o ResponseEntity<?> (cualquiera de los dos es correcto)
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Hola desde DiccionariosRestController"); // ResponseEntity.ok devuelve un código de estado 200
        // Hay muchas otras opciones para devolver: 
        // ResponseEntity.notFound() devuelve un código de estado 404
        // ResponseEntity.status(201).body("Error interno del servidor") devuelve
    }


    // PAra cada ruta, tengo que declarar una función (con el nombre que quiera usar)
    // Y esa fdunción la tengo que ANOTAR dependiendo del VERBO/METODO HTTP que vaya a atender en su ruta:
    @GetMapping("/diccionarios/{idioma}") // Voy a declarar la ruta /diccionarios/<idioma> que atiende el verbo HTTP GET
    public ResponseEntity<Void> existeDiccionarioDe(@PathVariable("idioma") String idioma) {
                                                // Este idioma, que es un parámetro JAVA de mi función, 
                                                // debe obtenerse de la RUTA HTTP que ha sido invocada.
        /*if(idioma.equals("es")) {
            return ResponseEntity.ok().build(); // ResponseEntity.ok devuelve un código de estado 200
        } else {
            return ResponseEntity.notFound().build(); // ResponseEntity.notFound() devuelve un código de estado 404
        }
            Este código hay que reemplazarlo por una llamada a un suministrador de diccionarios.
        */
       if(suministradorDeDiccionarios.tienesDiccionarioDe(idioma)) {
            return ResponseEntity.ok().build(); // ResponseEntity.ok devuelve un código de estado 200
        } else {
            return ResponseEntity.notFound().build(); // ResponseEntity.notFound() devuelve un código de estado 404
        }
    }

    
    @GetMapping("/diccionarios/{idioma}/{palabra}")
    public ResponseEntity<RespuestaPalabra> existePalabra(@PathVariable("idioma") String idioma, @PathVariable("palabra") String palabra) {
        if(!suministradorDeDiccionarios.tienesDiccionarioDe(idioma)) {
            return ResponseEntity.status(404).body(new RespuestaPalabra()); // ResponseEntity.notFound() devuelve un código de estado 404
        } else {
            // Extraigo el diccionario de ese idioma
            Diccionario diccionario = suministradorDeDiccionarios.dameDiccionarioDe(idioma).get();
            if(!diccionario.existe(palabra)) {
                return ResponseEntity.status(404).body(new RespuestaPalabra(idioma)); // ResponseEntity.notFound() devuelve un código de estado 404
            } else {
                // La palabra existe en el diccionario
                // OJO con el orden de los argumentos: el constructor es (palabra, idioma, significados)
                RespuestaPalabra respuesta = new RespuestaPalabra(palabra, idioma, diccionario.dameSignificados(palabra).get());
                return ResponseEntity.ok(respuesta); // ResponseEntity.ok devuelve un código de estado 200 y pone en el BODY de la respuesta HTTP el
            }
        }
    }
    // Spring (haciendo uso de una librería llamada Jackson) se encarga de convertir el objeto RespuestaPalabra en un JSON y ponerlo en el BODY de la respuesta HTTP.

}
