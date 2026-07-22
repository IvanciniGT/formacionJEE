import java.util.Optional;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.HashMap;
import java.util.List;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

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
            return ficheroDelDiccionarioParaElIdioma(idioma).exists();
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
        // Lo primero que necesito es el fichero de texto que contiene el diccionario de ese idioma.
        File diccionario = ficheroDelDiccionarioParaElIdioma(idioma);
        // Al leer el fichero puede haber problemas:
        // - Permisos
        // - El archivo por dentro no tiene el formato que esperamos
        // No debería pasar este problema. NUNCA.
        // Si pasa, es un problema de configuración o un problema de desarrollo del diccionario (BUG)
        // Genero una Excepcion (ALGO EXCEPCIONAL! que no debería pasar... y además que a priori no tengo forma de saber si va a ocurrir)
        BufferedReader lectorDeLineas = null;
        try{ // Intenta hacer este trabajo que vamos aponer dentro de las {}
            lectorDeLineas = new BufferedReader(new FileReader(diccionario));
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
    
    private File ficheroDelDiccionarioParaElIdioma(String idioma){
        return new File(carpetaDeLosDiccionarios + File.separator + idioma + ".txt");
                                                // Este File.separator nos da el separador del sistema operativo donde se está ejecutando la aplicación: / \ ...
    } // DRY = Don't Repeat Yourself. No repetir código. Si hay un trozo de código que se repite, lo metemos en una función y la llamamos desde los distintos sitios donde se repite.
    
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
