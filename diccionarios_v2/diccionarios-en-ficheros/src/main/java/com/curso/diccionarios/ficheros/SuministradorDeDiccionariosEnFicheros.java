package com.curso.diccionarios.ficheros;

import java.util.Optional;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.HashMap;
import java.util.List;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.curso.diccionarios.api.Diccionario;
import com.curso.diccionarios.api.SuministradorDeDiccionarios;

public class SuministradorDeDiccionariosEnFicheros implements SuministradorDeDiccionarios {

    private String carpetaDeLosDiccionarios;
    private Map<String, Diccionario> cacheDeDiccionarios = new WeakHashMap<>();

    public SuministradorDeDiccionariosEnFicheros(String carpetaDeLosDiccionarios) {
        this.carpetaDeLosDiccionarios = carpetaDeLosDiccionarios;
    }

    public boolean tienesDiccionarioDe(String idioma){
        if(cacheDeDiccionarios.containsKey(idioma)) {
            return true;
        } else {
            // Si el recurso existe en el classpath, getResource nos devuelve su URL.
            // Si no existe, nos devuelve null. Ese null es nuestro "no lo tengo".
            return cargadorDeClases().getResource(rutaDelRecursoParaElIdioma(idioma)) != null;
        }
    }

    public Optional<Diccionario> dameDiccionarioDe(String idioma){
        if(!tienesDiccionarioDe(idioma)) {
            return Optional.empty(); // No devolver nada = Devolver un Optional vacío
        } else {
            // Si no lo tengo en cache, lo cargo del fichero y lo meto en cache
            if(!cacheDeDiccionarios.containsKey(idioma)) {
                // Lo cargo y lo meto en cache (Leer el fichero)
                cargarFicheroDeDiccionarioEnCache(idioma);
            }
            // Después, siempre lo devuelvo desde la cache!
            return Optional.of(cacheDeDiccionarios.get(idioma));
        }
    }
    
    private void cargarFicheroDeDiccionarioEnCache(String idioma){
        // Lo primero que necesito es el flujo (stream) de bytes del recurso que contiene el diccionario de ese idioma.
        // Al leer el recurso puede haber problemas:
        // - El archivo por dentro no tiene el formato que esperamos
        // No debería pasar este problema. NUNCA.
        // Si pasa, es un problema de configuración o un problema de desarrollo del diccionario (BUG)
        // Genero una Excepcion (ALGO EXCEPCIONAL! que no debería pasar... y además que a priori no tengo forma de saber si va a ocurrir)
        BufferedReader lectorDeLineas = null;
        try{ // Intenta hacer este trabajo que vamos aponer dentro de las {}
            // Abrimos el recurso como un stream (funciona igual estando suelto en disco o dentro de un .jar).
            InputStream flujoDelDiccionario = cargadorDeClases().getResourceAsStream(rutaDelRecursoParaElIdioma(idioma));
            // Leemos SIEMPRE en UTF-8, para que las tildes y la ñ se lean correctamente (melón, español...).
            lectorDeLineas = new BufferedReader(new InputStreamReader(flujoDelDiccionario, StandardCharsets.UTF_8));
            String linea;
            Map<String, List<String>> tablaDePalabrasYSignificados = new HashMap<>();
            while((linea = lectorDeLineas.readLine()) != null) { // Cuando no haya más lineas nos devuelve null y paramos
                // Tomo la linea y la parto por el signo "=".
                // Lo primero es la palabra, y lo segundo son los significados.
                String[] partes = linea.split("=");
                String palabra = partes[0];
                List<String> significados = List.of(partes[1].split("\\|"));
                tablaDePalabrasYSignificados.put(palabra, significados);
            }
            lectorDeLineas.close();
            // Ahora nos hace falta meterlo en cache!
            Diccionario diccionarioRecienLeido = new DiccionarioEnFichero(idioma, tablaDePalabrasYSignificados);
            cacheDeDiccionarios.put(idioma, diccionarioRecienLeido);
        } catch(Exception e) {  // Caso que haya cualquier tipo. de problema, generamos un error en tiempo de ejecución
                                // No quiero darle trámite... quiero NOTIFICARLO hasta sus últimas consecuencias...
                                // Es un bug lo que hay aquí.
            // Deberíamos intentar cerrarlo... si que es que hemos llegado a abrirlo
            try{
                lectorDeLineas.close();
            } catch(Exception errorAlCerraElFichero) {
                // No hacemos nada. No podemos hacer nada. Ya hemos tenido un error... y ahora otro error al cerrar el fichero.
            }
            throw new RuntimeException("Error al leer el fichero de diccionario de idioma " + idioma + " en la carpeta " + carpetaDeLosDiccionarios, e);
        }
    }
    
    private String rutaDelRecursoParaElIdioma(String idioma){
        return carpetaDeLosDiccionarios + "/" + idioma + ".txt";
        // OJO: dentro del classpath (y del .jar) el separador SIEMPRE es "/", nunca File.separator.
        // Un .jar es un zip y sus rutas internas usan "/" en cualquier sistema operativo.
    } // DRY = Don't Repeat Yourself. No repetir código. Si hay un trozo de código que se repite, lo metemos en una función y la llamamos desde los distintos sitios donde se repite.

    private ClassLoader cargadorDeClases(){
        // El cargador de clases sabe encontrar recursos que estén en el classpath (sueltos o dentro del .jar).
        return SuministradorDeDiccionariosEnFicheros.class.getClassLoader();
    }
    
    // La app que estoy montando solo busca una palabra en un diccionario... y acaba.
    // Desde el punto de vista del suministrador eso no es relevante.
    // el día de mañana, este suministrador, podría estar siendo invocado desde una aplicación que busque 1000 palabras en 1000 diccionarios distintos.
    // No sé como va a evolucionar esto. 
    // Yo lo único que me concentro es en la funcionalidad del suministrador.

    // Lo que si me planteo es que tengo que leer ficheros de texto.
    // Y leer un fichero lleva su tiempo.
    // Y si ya he leído un diccionario, no tiene sentido que lo vuelva a leer más adelante, si me preguntan por una nueva palabra en el mismo diccionario (fichero).
    // De alguna forma, me puede interesar, ir guardando en memoria los ficheros que vaya leyendo.
    // De esta forma, si me preguntan por un diccionario que ya he leído, puedo dar una respuesta mucho más rápida.

    // Lo que vamos a crear es una "CACHE"

    // Nuestros diccionarios estaran en dicheros txt, cuyo nombre será el idioma del diccionario.
    // Las palabras estarán cada una en una linea del fichero, separadas con un "=" de las definiciones/significados.
    // Caso que haya más de 1 significado, estarán separados por "|".

    // Un diccionario se identifica por el idioma (String)
    // Necesito en memoria (CACHE) una tabla de diccionarios.
    // Donde cada diccionario se identifica por el idioma (String)
}
