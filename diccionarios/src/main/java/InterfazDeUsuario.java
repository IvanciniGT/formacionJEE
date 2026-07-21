import java.util.List;
import java.util.Optional;

public interface InterfazDeUsuario {

    // void, significa que la función NO DEVUELVE NADA...
    // Hace su trabajo y ya!
    void mostrarMensajeBienvenida();
    void mostrarMensajeDespedida();
    void mostrarMensajeAyuda();
    void mostrarQueLaPalabraExiste(String palabra, String idioma);
    void mostrarQueLaPalabraNoExiste(String palabra, String idioma);
    void mostrarSignificados(List<String> significados);
    void mostrarQueNoTengoDiccionarioDe(String idioma);

    Optional<String> recuperarLaPalabraSolicitadaPorElUsuario();
    Optional<String> recuperarElIdiomaSolicitadoPorElUsuario();
}
