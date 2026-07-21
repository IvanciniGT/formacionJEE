import java.util.Optional;
import java.util.List;

public class Aplicacion {

    // La aplicación para funcionar necesita de dos componentes:
    private static InterfazDeUsuario           interfazDeUsuario;
    private static SuministradorDeDiccionarios suministradorDeDiccionarios;

    // Cuando se ejecuta la aplicación:
    public static void main(String[] args) {
        interfazDeUsuario.mostrarMensajeBienvenida();

        Optional<String> palabraDelUsuario = interfazDeUsuario.recuperarLaPalabraSolicitadaPorElUsuario();
        if(palabraDelUsuario.isEmpty()) {
            interfazDeUsuario.mostrarMensajeAyuda();
        } else {
            Optional<String> idiomaDelUsuario = interfazDeUsuario.recuperarElIdiomaSolicitadoPorElUsuario();
            if(idiomaDelUsuario.isEmpty()) {
                interfazDeUsuario.mostrarMensajeAyuda();
            } else {
                // Es decir, si si ha puesto palabra y si ha puesto idioma: 
                boolean tengoDiccionarioDeEseIdioma = suministradorDeDiccionarios.tienesDiccionarioDe(idiomaDelUsuario.get());
                if(!tengoDiccionarioDeEseIdioma) { // ! Significa NO
                    interfazDeUsuario.mostrarQueNoTengoDiccionarioDe(idiomaDelUsuario.get());
                } else { // Si si que tengo diccionario de ese idioma:
                    Diccionario diccionario = suministradorDeDiccionarios.dameDiccionarioDe(idiomaDelUsuario.get()).get();
                    boolean existeLaPalabra = diccionario.existe(palabraDelUsuario.get());
                    if(!existeLaPalabra) {
                        interfazDeUsuario.mostrarQueLaPalabraNoExiste(palabraDelUsuario.get(), idiomaDelUsuario.get());
                    } else {
                        interfazDeUsuario.mostrarQueLaPalabraExiste(palabraDelUsuario.get(), idiomaDelUsuario.get());
                        List<String> significados = diccionario.dameSignificados(palabraDelUsuario.get()).get();
                        interfazDeUsuario.mostrarSignificados(significados);
                    }
                }
            }
            interfazDeUsuario.mostrarMensajeDespedida();
        }
    }

}
