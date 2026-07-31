package com.curso.diccionarios.ui.escritorio;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import com.curso.diccionarios.ui.InterfazDeUsuario;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * ============================================================================
 * ACTO 1 — La promesa del día 1, cumplida al pie de la letra.
 * ============================================================================
 *
 * El diseño del README (día 1) decía esto:
 *
 *     +----> InterfazDeUsuario   &lt;----- InterfazDeUsuarioConsola
 *                                &lt;----- InterfazDeUsuarioDeEscritorio
 *
 * Pues aquí está la segunda implementación. Esta clase implementa EXACTAMENTE
 * el mismo contrato que InterfazDeUsuarioConsola, sin cambiar ni una coma de:
 *
 *   - ui-api (el contrato)
 *   - Aplicacion (el algoritmo de la aplicación)
 *   - diccionarios-api, el servidor, la BBDD, el cliente HTTP...
 *
 * Lo ÚNICO que cambia en todo el sistema es una línea en InterfazDeUsuarioFactory.
 * El diseño era correcto: la sustitución funciona.
 *
 * ----------------------------------------------------------------------------
 * PERO FIJAOS EN LO QUE PASA AL EJECUTARLO
 * ----------------------------------------------------------------------------
 *
 * Sale una aplicación de escritorio RARÍSIMA: se abre, buscas UNA palabra, y
 * la aplicación se queda muerta. No puedes buscar una segunda palabra.
 *
 * Y no es culpa de JavaFX ni de esta clase. Es culpa del CONTRATO:
 *
 *   1. `recuperarLaPalabraSolicitadaPorElUsuario()` es una pregunta que se
 *      hace UNA VEZ. Está pensada para leer argumentos de línea de comandos.
 *      Aquí tenemos que BLOQUEAR el programa hasta que el usuario pulse un
 *      botón (mirad el CountDownLatch de abajo: eso es un parche).
 *
 *   2. `Aplicacion.main()` es un GUION que se ejecuta de arriba abajo y
 *      termina. En consola eso está bien. En escritorio, el usuario espera
 *      poder seguir usando la ventana.
 *
 *   3. El contrato no tiene forma de preguntar QUÉ IDIOMAS HAY. Por eso el
 *      idioma va en una caja de texto libre, y no en un desplegable: la UI no
 *      tiene acceso a la capa de negocio. Sólo sabe MOSTRAR lo que le mandan.
 *
 *   4. Si dejáis los campos vacíos, `Aplicacion` llamará a
 *      `mostrarMensajeAyuda()`, que en el contrato significa "explica cómo se
 *      usan los argumentos de línea de comandos"... en una ventana gráfica.
 *
 * CONCLUSIÓN: el contrato InterfazDeUsuario no describe "una interfaz de
 * usuario". Describe "una interfaz de usuario DE CONSOLA, gobernada por un
 * guion". Es una abstracción con la forma de su primera implementación.
 *
 * En el ACTO 2 (VentanaDeDiccionarios) se ve cómo se resuelve esto de verdad.
 */
public class InterfazDeUsuarioEscritorio implements InterfazDeUsuario {

    /*
     * Un CountDownLatch es un semáforo de un solo uso: un hilo espera en él
     * hasta que otro hilo lo abre. Lo necesitamos porque en una aplicación
     * gráfica hay DOS HILOS DE EJECUCIÓN a la vez:
     *
     *   - El hilo de Aplicacion.main(), que ejecuta el guion de la aplicación.
     *   - El hilo de JavaFX, que dibuja la ventana y atiende los clics.
     *
     * En consola sólo había uno. Que aquí hagan falta dos, y un semáforo para
     * sincronizarlos, es la primera señal de que estamos forzando el contrato.
     */
    private final CountDownLatch ventanaConstruida = new CountDownLatch(1);
    private final CountDownLatch usuarioHaPulsadoBuscar = new CountDownLatch(1);

    // volatile: avisa a la JVM de que estas variables se escriben en un hilo
    // y se leen en otro, y que por tanto no puede cachearlas.
    private volatile String palabraSolicitada;
    private volatile String idiomaSolicitado;

    private TextField campoPalabra;
    private TextField campoIdioma;
    private Button botonBuscar;
    private VBox panelDeResultados;

    /**
     * La factoría llama a este constructor pasándole los argumentos de la línea
     * de comandos, igual que a InterfazDeUsuarioConsola. Aquí no nos hacen
     * falta (los datos los pide la ventana), pero mantenemos la firma para que
     * la factoría no tenga que cambiar de forma: sólo de implementación.
     */
    public InterfazDeUsuarioEscritorio(String[] argumentosDeLaAplicacion) {
        // Arranca el motor gráfico de JavaFX y ejecuta construirVentana() en su
        // propio hilo. A partir de aquí ya hay dos hilos vivos.
        Platform.startup(this::construirVentana);
        esperarA(ventanaConstruida);
    }

    // ------------------------------------------------------------------
    // Construcción de la ventana (se ejecuta en el hilo de JavaFX)
    // ------------------------------------------------------------------

    private void construirVentana() {
        Label titulo = new Label("Diccionarios");
        titulo.getStyleClass().add("titulo");

        Label subtitulo = new Label("ACTO 1 · implementando el contrato InterfazDeUsuario tal cual");
        subtitulo.getStyleClass().add("subtitulo");

        campoPalabra = new TextField();
        campoPalabra.setPromptText("Palabra a buscar");
        HBox.setHgrow(campoPalabra, Priority.ALWAYS);

        campoIdioma = new TextField();
        campoIdioma.setPromptText("Idioma (es, en, elfico)");
        campoIdioma.setPrefWidth(190);

        botonBuscar = new Button("Buscar");
        botonBuscar.getStyleClass().add("boton-primario");
        botonBuscar.setDefaultButton(true);
        botonBuscar.setOnAction(evento -> capturarLoQueHaPedidoElUsuario());

        HBox formulario = new HBox(10, campoPalabra, campoIdioma, botonBuscar);

        panelDeResultados = new VBox(10);
        panelDeResultados.getStyleClass().add("panel-resultados");

        ScrollPane zonaScroll = new ScrollPane(panelDeResultados);
        zonaScroll.setFitToWidth(true);
        zonaScroll.getStyleClass().add("zona-scroll");
        VBox.setVgrow(zonaScroll, Priority.ALWAYS);

        VBox raiz = new VBox(16, titulo, subtitulo, formulario, zonaScroll);
        raiz.setPadding(new Insets(24));
        raiz.getStyleClass().add("raiz");

        Scene escena = new Scene(raiz, 720, 520);
        escena.getStylesheets().add(
                getClass().getResource("/escritorio/estilos.css").toExternalForm());

        Stage escenario = new Stage();
        escenario.setTitle("Diccionarios — Acto 1");
        escenario.setScene(escena);
        escenario.setOnCloseRequest(evento -> {
            Platform.exit();
            System.exit(0);
        });
        escenario.show();

        ventanaConstruida.countDown();
    }

    /**
     * Se ejecuta cuando el usuario pulsa "Buscar". Guarda lo que ha escrito y
     * LIBERA al hilo de Aplicacion, que llevaba todo este rato bloqueado
     * esperando dentro de recuperarLaPalabraSolicitadaPorElUsuario().
     *
     * Y deshabilita el formulario, porque el guion de Aplicacion sólo se
     * ejecuta UNA VEZ: una segunda búsqueda no llegaría a ninguna parte.
     */
    private void capturarLoQueHaPedidoElUsuario() {
        palabraSolicitada = campoPalabra.getText();
        idiomaSolicitado = campoIdioma.getText();

        campoPalabra.setDisable(true);
        campoIdioma.setDisable(true);
        botonBuscar.setDisable(true);

        usuarioHaPulsadoBuscar.countDown();
    }

    // ------------------------------------------------------------------
    // El contrato InterfazDeUsuario
    // ------------------------------------------------------------------

    @Override
    public void mostrarMensajeBienvenida() {
        escribir("Aplicación de Diccionarios v1.1.0", "linea-destacada");
    }

    @Override
    public void mostrarMensajeDespedida() {
        escribir("Gracias por usar nuestra aplicación de diccionarios.", "linea-destacada");
        escribir("⚠️  Aplicacion.main() ha terminado. El formulario ya no responde: "
                + "el guion de la aplicación se ejecuta UNA sola vez. "
                + "Cierra la ventana y pasamos al Acto 2.", "linea-aviso");
    }

    @Override
    public void mostrarMensajeAyuda() {
        // Este texto está copiado de InterfazDeUsuarioConsola a propósito.
        // Es lo que el contrato entiende por "ayuda": explicar los argumentos
        // de la línea de comandos. En una ventana gráfica no tiene ningún
        // sentido, y es una consecuencia directa de heredar el contrato.
        escribir("No ha suministrado los parámetros necesarios.", "linea-aviso");
        escribir("Debe suministrar la palabra a buscar y el idioma del diccionario.", "linea");
        escribir("Ejemplo:", "linea");
        escribir("    c:\\> buscarPalabra \"melón\" \"es\"", "linea-codigo");
    }

    @Override
    public void mostrarQueLaPalabraExiste(String palabra, String idioma) {
        escribir("La palabra " + palabra + " existe en el diccionario de " + idioma
                + ", y significa:", "linea-destacada");
    }

    @Override
    @Deprecated
    public void mostrarQueLaPalabraNoExiste(String palabra, String idioma) {
        mostrarQueLaPalabraNoExiste(palabra, idioma, null);
    }

    @Override
    public void mostrarQueLaPalabraNoExiste(String palabra, String idioma, List<String> similares) {
        escribir("La palabra " + palabra + " no existe en el diccionario de " + idioma + ".",
                "linea-destacada");
        if (similares != null && !similares.isEmpty()) {
            escribir("Quizás quiso decir alguna de estas palabras:", "linea");
            for (String similar : similares) {
                escribir("— " + similar.toLowerCase(), "linea");
            }
        }
    }

    @Override
    public void mostrarSignificados(List<String> significados) {
        for (String significado : significados) {
            escribir("— " + significado, "linea");
        }
    }

    @Override
    public void mostrarQueNoTengoDiccionarioDe(String idioma) {
        escribir("Lo siento, pero no tengo diccionario " + idioma + ".", "linea-aviso");
    }

    @Override
    public Optional<String> recuperarLaPalabraSolicitadaPorElUsuario() {
        // AQUÍ ESTÁ EL PROBLEMA DE FONDO, EN UNA SOLA LÍNEA:
        // el contrato dice "devuélveme la palabra", así que no nos queda otra
        // que CONGELAR el programa hasta que el usuario pulse el botón.
        esperarA(usuarioHaPulsadoBuscar);
        return textoUtil(palabraSolicitada);
    }

    @Override
    public Optional<String> recuperarElIdiomaSolicitadoPorElUsuario() {
        // El idioma ya lo capturamos a la vez que la palabra, así que aquí no
        // hay que esperar nada. Otra señal de que el contrato (dos preguntas
        // independientes) no encaja con un formulario (una sola interacción).
        return textoUtil(idiomaSolicitado);
    }

    // ------------------------------------------------------------------
    // Utilidades internas
    // ------------------------------------------------------------------

    /**
     * Añade una línea al panel de resultados.
     *
     * Toda modificación de la ventana DEBE hacerse en el hilo de JavaFX, y
     * estos métodos los está llamando el hilo de Aplicacion. Platform.runLater
     * es el mensajero entre los dos hilos.
     */
    private void escribir(String texto, String claseDeEstilo) {
        Platform.runLater(() -> {
            Label linea = new Label(texto);
            linea.setWrapText(true);
            linea.getStyleClass().add(claseDeEstilo);
            panelDeResultados.getChildren().add(linea);
        });
    }

    private Optional<String> textoUtil(String texto) {
        if (texto == null || texto.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(texto.trim());
    }

    private void esperarA(CountDownLatch semaforo) {
        try {
            semaforo.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("La espera de la interfaz gráfica se ha interrumpido", e);
        }
    }
}
