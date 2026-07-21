import java.util.List;
import java.util.Optional;

public class InterfazDeUsuarioConsola implements InterfazDeUsuario {

    public void mostrarMensajeBienvenida(){
        System.out.println("Aplicación de Diccionarios v1.1.0" ); // Imprimir en la consola. En el canal estandar de salida del proceso/aplicación.
    }
    public void mostrarMensajeDespedida(){
        System.out.println("Gracias por usar nuestra aplicación de diccionarios." ); // Imprimir en la consola. En el canal estandar de salida del proceso/aplicación.
    }
    public void mostrarMensajeAyuda(){
        System.out.println("No ha suministrado los parámetros necesarios");
        System.out.println("Debe suministrar la palabra a buscar y el idioma del diccionario.");
        System.out.println("Ejemplo:");
        System.out.println("    c:\\> buscarPalabra \"melón\" \"es\"");

    }
    public void mostrarQueLaPalabraExiste(String palabra, String idioma){
        System.out.println("La palabra " + palabra + " existe en el diccionario de " + idioma + ", y significa:");
    }
    public void mostrarQueLaPalabraNoExiste(String palabra, String idioma){
        System.out.println("La palabra " + palabra + " no existe en el diccionario de " + idioma + ".");
    }
    public void mostrarSignificados(List<String> significados){
        for(String significado : significados) {
            System.out.println("- " + significado);
        }
    }
    public void mostrarQueNoTengoDiccionarioDe(String idioma){
        System.out.println("Lo siento, pero no tengo diccionario " + idioma + ".");
    }

    public Optional<String> recuperarLaPalabraSolicitadaPorElUsuario(){

    }
    public Optional<String> recuperarElIdiomaSolicitadoPorElUsuario(){

    }
}
