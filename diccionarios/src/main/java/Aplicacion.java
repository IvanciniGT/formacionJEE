import java.util.Optional;
import java.util.List;

public class Aplicacion {

    // La aplicación para funcionar necesita de dos componentes:
    private static InterfazDeUsuario           interfazDeUsuario;
    private static SuministradorDeDiccionarios suministradorDeDiccionarios;

    // Cuando se ejecuta la aplicación:
    public static void main(String[] args) {

        // Esto podría hacerlo... pero sería un mal diseño. No lo queremos!
        // Este archivo está tomando más responsabilides de las que debería. 
        // Creamos los componentes que necesitamos:
        //interfazDeUsuario               = new InterfazDeUsuarioConsola(args);
        //suministradorDeDiccionarios     = new SuministradorDeDiccionariosEnFicheros("diccionarios");
        // Cual es el problema?
        // - Si el día de mañana, Cambio el SuministradorDeDiccionariosEnFicheros por un SuministradorDeDiccionariosEnBBDD,
        // Tengo que venir a este fichero a cambiarlo!
        // - Si el día de mañana, Cambio el InterfazDeUsuarioConsola por un InterfazDeUsuarioDesktop, tengo que venir a este fichero a cambiarlo!
        // Es decir, tengo COMPONENTESD QUE ESTAN ACOPLADOS.
        // Es como si por querer cambiar la bateria de un coche, tengo que cambiar la disposición de otros elementos 
        // dentro del capo del coche, porque no entra y tengo que hacer hueco.
        // El cambio debería ser más fácil.
        // Esta app que hemos montado es muy simple! Tiene 4 componentes.
        // En una aplicación REAL, con decenas o cientos de componentes, si empezamos a hacer esto, ESTAMOS MUERTOS!
        // Cada cosa que tenga que cambiarse en el futuro (Y LAS HABRÁ) nos va a obligar a tocar cosas en otros tantos ficheros. 
        // ESO ES UN PROBLEMON! NO TRABAJAMOS ASI!
        // Resolución del problema con un patrón factoria:
        interfazDeUsuario               = InterfazDeUsuarioFactory.dameInterfazDeUsuario(args);
        suministradorDeDiccionarios     = SuministradorDeDiccionariosFactory.dameSuministradorDeDiccionarios();
        
        // Ejecutamos la aplicación:

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
