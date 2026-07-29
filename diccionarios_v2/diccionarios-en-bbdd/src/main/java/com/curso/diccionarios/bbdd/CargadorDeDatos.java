package com.curso.diccionarios.bbdd;

import org.springframework.boot.CommandLineRunner;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.curso.diccionarios.bbdd.entidades.Idioma;
import com.curso.diccionarios.bbdd.entidades.Palabra;
import com.curso.diccionarios.bbdd.entidades.Significado;
import com.curso.diccionarios.bbdd.repositorios.IdiomaRepository;
import com.curso.diccionarios.bbdd.repositorios.PalabraRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
// En automático Spring, al identificar este componente de mi aplicación (por tener la anotación @Component),
// Y ser un CommandLineRunner, va a invocar la función run() de esta clase, cuando arranque la aplicación.
public class CargadorDeDatos implements CommandLineRunner{

    private final Logger logger = LoggerFactory.getLogger(CargadorDeDatos.class);

    private final IdiomaRepository idiomaRepository;
    private final PalabraRepository palabraRepository;
    // Haré que Spring me inyecte el valor en esta variable.
    // En base a lo que se haya definido en el archivo application.properties.
    // Diré que el valor de esta variable esté relacionado sea) el valor
    // de la propiedad diccionarios.carpeta que se haya definido en el archivo application.properties.
    // Incluso diré que si no se ha definido archivo application.properties
    // o que si no existe dentro de ese archivo la propiedad diccionarios.carpeta
    // , que el valor por defecto de esta variable sea "diccionarios".
    // PAra ello Spring me regala otra anotacion: @Value("${diccionarios.carpeta:diccionarios}")
    @Value("${diccionarios.carpeta:diccionarios}")
    // ${diccionarios.carpeta:diccionarios}
        // diccionarios.carpeta <- El valor de la variable debe ser el de la propiedad diccionarios.carpeta que se haya definido en el archivo application.properties.
        // :diccionarios <- Si no se ha definido la propiedad diccionarios.carpeta, se usará "diccionarios" como valor por defecto.

    private String carpetaDeDiccionarios;
    
    public CargadorDeDatos(IdiomaRepository idiomaRepository, PalabraRepository palabraRepository) { // INYECCION DE DEPDNENCIAS
        this.idiomaRepository = idiomaRepository;
        this.palabraRepository = palabraRepository;
    }


    // Esta función es la que se invocará en automático por SpringBoot cuando arranque la aplicación.
    // Yo no tengo que preocuparme de ello.
    // Lo ñunico que si necesito hacer es indicar en este archivo, que 
    // Esta clase define un componente de mi aplciación
    // Para ello, pondré la anotación @Component antes del nombre de la clase.

    // Sería interesante ir generando algo de información en el log de la aplicación, para saber que se está ejecutando esta función y que está cargando
    // o no los datos en la BBDD.
    // También me podría interesar saber cuantos datos de han cargado de cada idioma, y cuantos idiomas se han cargado en total.
    // Para ello, necesito un objeto que me permita escribir en el log de la aplicación.
    // Spring me regala un objeto Logger que puedo usar para escribir en el log de la aplicación.
    // Ese objeto Logger lo puedo obtener de la clase LoggerFactory de Spring.
    // Para ello, voy a crear un objeto Logger en esta clase, y lo voy a inicializar con el objeto LoggerFactory de Spring, pasándole como parámetro el nombre de esta clase
    public void run(String... args) throws Exception {

        logger.info("Cargando datos iniciales en la BBDD desde la carpeta de diccionarios: " + carpetaDeDiccionarios);

        // ANTES DE NADA MIRO SI YA HAY DATOS EN LA BBDD. SI HAY DATOS, NO HAGO NADA.
        long numeroDeIdiomas = idiomaRepository.count();
        if(numeroDeIdiomas > 0){
            // Si ya hay datos en la BBDD, no hago nada.
            logger.info("Ya hay datos en la BBDD. No se cargan datos iniciales.");
            return;
        }



        // Aquí es donde vamos a cargar los datos iniciales de la base de datos.
        // NEcezitamos acceder a la carpeta de los diccionarios, y leer los ficheros de cada idioma, y cargar los datos en la base de datos.
        // Vamos a necesitar unas funciones muy similares a las que ya tenemos en el proyecto diccionarios-en-ficheros, 
        // para leer los ficheros de cada idioma y cargar los datos en la base de datos.
        // Eso si... una vez leidos los ficheros, para cargar los datos en las tablas de la BBDD
        // Vamos a necesitar acceso a los repositorios de las entidades de la BBDD, para poder guardar los datos en la BBDD.
        // Hagamos que Spring en automático nos inyecte los repositorios de las entidades de la BBDD en esta clase, para poder usarlos aquí.
        
        // Lo primero sería ver que diccionarios/arcvhivos/idiomas hay en la carpeta de diccionarios. 
        // Para ello, vamos a necesitar una función que nos devuelva un listado de los idiomas que hay en la carpeta de diccionarios.
        List<String> idiomas = listadoDeIdiomas(carpetaDeDiccionarios);
        logger.info("Se van a cargar los siguientes idiomas en la BBDD: " + idiomas);
        // Para cada uno de los idiomas, vamos a leer el fichero de ese idioma, y vamos a cargar los datos en la BBDD.
        for(String idioma : idiomas){
            Map<String, List<String>> palabrasYSignificados = leerFicheroDeIdioma(carpetaDeDiccionarios, idioma);
            // Dar de alta el idioma en la tabla de idiomas, si no existe ya.
            Idioma idiomaEntity = new Idioma();
            // CAMBIO: Voy a guardar el código del Idioma en mayúsculas
            idiomaEntity.setCodigo(NormalizadorDeTerminos.normalizar(idioma));
            idiomaRepository.save(idiomaEntity);
            // Para cada palabra y sus significados, vamos a dar de alta la palabra en la tabla de palabras, con sus significados en la tabla de significados.
            for(Map.Entry<String, List<String>> entrada : palabrasYSignificados.entrySet()){
                String palabra = entrada.getKey();
                List<String> significados = entrada.getValue();
                // Dar de alta la palabra en la tabla de palabras, con sus significados en la tabla de significados.
                // Para ello, vamos a necesitar una función que nos permita dar de alta una palabra con sus significados en la BBDD.
                // Esa función la vamos a implementar en el repositorio de palabras.
                Palabra palabraEntity = new Palabra();
                // CAMBIO: Voy a guardar la palabra en mayúsculas
                palabraEntity.setPalabra(NormalizadorDeTerminos.normalizar(palabra));
                palabraEntity.setIdioma(idiomaEntity);

                // Preparo los significados ANTES de guardar la palabra,
                // y los engancho a la palabra (los dos lados de la relación).
                List<Significado> significadosEntity = new ArrayList<>();
                for(String significado : significados){
                    Significado significadoEntity = new Significado();
                    significadoEntity.setSignificado(significado);
                    significadoEntity.setPalabra(palabraEntity);
                    significadosEntity.add(significadoEntity);
                }
                palabraEntity.setSignificados(significadosEntity);

                // Un único save: gracias al cascade declarado en la entidad Palabra,
                // al guardar la palabra se guardan también sus significados.
                palabraRepository.save(palabraEntity);
            }
            logger.info("Se han cargado " + palabrasYSignificados.size() + " palabras y sus significados para el idioma " + idioma);
        }
    }

    private List<String> listadoDeIdiomas(String carpetaDeDiccionarios) {
        // Luego nos preocupamos de implementar esta función, que nos devuelva un listado de los idiomas que hay en la carpeta de diccionarios.
        // Es importante entender que la caprta de diccionarios es una carpeta que debe buscarse en el classpath de la aplicación. Es decir, debe estar en la carpeta src/main/resources de nuestro proyecto, para que al compilarse el proyecto, esa carpeta se copie en el classpath de la aplicación.
        // Pôdría incluso estar en un jar, y al ejecutarse el jar, esa carpeta de diccionarios debe estar en el classpath del jar.
        //Obtener un listado de los idiomas que hay en la carpeta de diccionarios, es decir, un listado de los nombres de los ficheros que hay en la carpeta de diccionarios.

        List<String> idiomas = new ArrayList<>();
        // Para ello, vamos a usar la clase ClassLoader de Java, que nos permite cargar recursos que estén en el classpath de la aplicación.
        // Vamos a usar el método getResource de la clase ClassLoader, que nos permite obtener un recurso que esté en el classpath de la aplicación, y nos devuelve un objeto URL que representa el recurso.
        // Luego, vamos a usar el método getFile de la clase URL, que nos permite obtener el nombre del fichero que representa el recurso.
        try {
            PathMatchingResourcePatternResolver buscadorDeRecursos = new PathMatchingResourcePatternResolver();

            Resource[] archivos = buscadorDeRecursos.getResources(
                    "classpath*:" + carpetaDeDiccionarios + "/*.txt"
            );

            for(Resource archivo : archivos){
                if(archivo.isReadable()){
                    String nombreDelArchivo = archivo.getFilename();

                    if(nombreDelArchivo != null){
                        idiomas.add(nombreDelArchivo.replace(".txt", ""));
                    }
                }
            }

            return idiomas;

        } catch(Exception e) {
            throw new RuntimeException(
                    "Error al obtener el listado de idiomas de la carpeta "
                            + carpetaDeDiccionarios,
                    e
            );
        }
        
    }

    private Map<String, List<String>> leerFicheroDeIdioma(String carpetaDeDiccionarios, String idioma) {
        // Este sería casi igual a la funcion cargarFicheroDeDiccionarioEnCache que tenemos en el proyecto diccionarios-en-ficheros, pero en lugar de cargarlo en cache, lo vamos a cargar en la BBDD.

        Map<String, List<String>> tablaDePalabrasYSignificados = new HashMap<>();
        try {
            Resource archivo = new ClassPathResource(
                    carpetaDeDiccionarios + "/" + idioma + ".txt"
            );

            BufferedReader lectorDeLineas = new BufferedReader(
                    new InputStreamReader(
                            archivo.getInputStream(),
                            StandardCharsets.UTF_8
                    )
            );

            String linea;
            while((linea = lectorDeLineas.readLine()) != null) { // Cuando no haya más lineas nos devuelve null y paramos
                // Tomo la linea y la parto por el signo "=".
                // Lo primero es la palabra, y lo segundo son los significados.
                String[] partes = linea.split("=");
                String palabra = partes[0];
                List<String> significados = List.of(partes[1].split("\\|"));
                tablaDePalabrasYSignificados.put(palabra, significados);
            }
            lectorDeLineas.close();
            return tablaDePalabrasYSignificados;
        } catch(Exception e) {  // Caso que haya cualquier tipo. de problema, generamos un error en tiempo de ejecución
                                // No quiero darle trámite... quiero NOTIFICARLO hasta sus últimas consecuencias...
                                // Es un bug lo que hay aquí.
            throw new RuntimeException("Error al leer el fichero de diccionario de idioma " + idioma + " en la carpeta " + carpetaDeDiccionarios, e);
        }
    }

}