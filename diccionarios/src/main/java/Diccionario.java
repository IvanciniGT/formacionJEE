import java.util.List;
import java.util.Optional;

// interface: Esto es una especifiación de lo que debe ser un diccionario.
// No es un diccionario. Ni un modelo de diccionario.
public interface Diccionario {

    String cualEsTuIdioma();

    boolean existe(String palabra);

    Optional<List<String>> dameSignificados(String palabra);
    // Si la plabara existe en el diccionario, devuelve una lista de significados.
    // Si la palabra no existe, no me devuelve la lista.

    // String, List o Optional son tipos de datos (clases/interfaces) de Java.
    // Son parte del lenguaje... Pero JAVA organiza muchos de estos tipos de datos en 
    // grupos (paquetes). Y para poder usarlos en mi fichero, necesito importarlos.
    // JAVA viene con más de 1000 tipos de datos, organizados en más de 100 paquetes.
    // Esto es algo que iremos aprendiendo con el tiempo (MUCHO TIEMPO)

    //int cuantasPalabrasTienes(); Esto podría ser algo que podamos preguntar a un diccionario...
    // Pero no es algo que nos interese ahora mismo para mi aplicación.
    // Si el día de mañana me hace falta, lo puedo añadir. Pero ahora no lo necesito.

}
