package com.curso.diccionarios.ui.escritorio;

import java.util.List;
import java.util.Optional;

import com.curso.diccionarios.api.Diccionario;
import com.curso.diccionarios.api.SuministradorDeDiccionarios;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * ============================================================================
 * ACTO 2 — La aplicación de escritorio de verdad.
 * ============================================================================
 *
 * En el Acto 1 descubrimos que el contrato `InterfazDeUsuario` no encaja con
 * una aplicación gráfica, porque está escrito para que la aplicación mande
 * sobre la interfaz: "dame la palabra", "muestra esto", y se acabó.
 *
 * En escritorio manda el usuario: busca, mira, pulsa una sugerencia, cambia de
 * idioma, vuelve a buscar. El flujo se INVIERTE. Y eso tiene nombre y lo
 * disteis en el día 4: INVERSIÓN DE CONTROL. Aquí ya no hay un guion nuestro
 * llamando a la UI; hay un framework (JavaFX) que nos llama a nosotros cuando
 * el usuario hace algo. Exactamente la diferencia entre librería y framework.
 *
 * Así que esta clase NO implementa `InterfazDeUsuario`. Consume directamente la
 * fachada de negocio `SuministradorDeDiccionarios`.
 *
 * Y ahí está el hallazgo de la sesión: ES LO MISMO QUE HIZO COPILOT CON ANGULAR.
 * Sin que nadie se lo dijera, la aplicación web tampoco implementó
 * `InterfazDeUsuario`: definió su propio contrato de UI y consumió
 * `SuministradorDeDiccionarios`. Dos interfaces de usuario independientes han
 * llegado a la misma conclusión.
 *
 * De las dos abstracciones que diseñasteis el día 1, la que ha sobrevivido a
 * tres clientes (consola, web y escritorio) es la de NEGOCIO, no la de UI.
 *
 * ----------------------------------------------------------------------------
 * Fijaos también en lo que esta clase NO sabe:
 *
 *   - No sabe qué es HTTP. No hay una sola URL en este archivo.
 *   - No sabe que hay un servidor, ni una base de datos, ni ficheros .txt.
 *   - No sabe si los diccionarios están a 3 milisegundos o a 3.000 kilómetros.
 *
 * Recibe un `SuministradorDeDiccionarios` por el constructor y punto: inyección
 * de dependencias, igual que el DiccionariosRestController del servidor. Quien
 * decide la implementación concreta es la factoría, en aplicacion-completa.
 *
 * Por eso este módulo reutiliza `diccionarios-en-servicio-web` TAL CUAL, sin
 * escribir una sola línea de HTTP nueva. La aplicación de Angular, al vivir
 * fuera de Java, tuvo que reescribir toda esa capa en TypeScript.
 */
public class VentanaDeDiccionarios {

    private final SuministradorDeDiccionarios suministradorDeDiccionarios;

    private ComboBox<String> selectorDeIdioma;
    private TextField campoPalabra;
    private Button botonBuscar;
    private VBox panelDeResultados;
    private ProgressIndicator indicadorDeProgreso;
    private VBox raiz;

    /*
     * Pequeña caché: `dameDiccionarioDe()` provoca una llamada al servidor para
     * comprobar que el idioma existe. Como el desplegable sólo ofrece idiomas
     * que el servidor ya nos ha dicho que tiene, guardamos el diccionario y nos
     * ahorramos ese viaje en cada búsqueda.
     *
     * Es la regla número uno del día 4: minimizar los viajes por la red.
     */
    private Diccionario diccionarioEnUso;
    private String idiomaDelDiccionarioEnUso;

    public VentanaDeDiccionarios(SuministradorDeDiccionarios suministradorDeDiccionarios) {
        this.suministradorDeDiccionarios = suministradorDeDiccionarios;
    }

    // ------------------------------------------------------------------
    // Construcción de la ventana
    // ------------------------------------------------------------------

    public void mostrarEn(Stage escenario) {
        Label titulo = new Label("📚  Diccionarios");
        titulo.getStyleClass().add("titulo");

        Label subtitulo = new Label("ACTO 2 · aplicación autónoma sobre SuministradorDeDiccionarios");
        subtitulo.getStyleClass().add("subtitulo");

        Button botonDeTema = new Button("🌙");
        botonDeTema.getStyleClass().add("boton-tema");
        botonDeTema.setOnAction(evento -> {
            boolean oscuro = raiz.getStyleClass().contains("oscuro");
            raiz.getStyleClass().removeAll("oscuro");
            if (!oscuro) {
                raiz.getStyleClass().add("oscuro");
            }
            botonDeTema.setText(oscuro ? "🌙" : "☀️");
        });

        Region separador = new Region();
        HBox.setHgrow(separador, Priority.ALWAYS);

        VBox textos = new VBox(2, titulo, subtitulo);
        HBox cabecera = new HBox(12, textos, separador, botonDeTema);
        cabecera.setAlignment(Pos.CENTER_LEFT);

        selectorDeIdioma = new ComboBox<>();
        selectorDeIdioma.setPromptText("Idioma");
        selectorDeIdioma.setPrefWidth(190);
        // Al cambiar de idioma se invalida la caché del diccionario.
        selectorDeIdioma.valueProperty().addListener((obs, anterior, nuevo) -> {
            if (nuevo != null && !nuevo.equals(idiomaDelDiccionarioEnUso)) {
                diccionarioEnUso = null;
                idiomaDelDiccionarioEnUso = null;
            }
        });

        campoPalabra = new TextField();
        campoPalabra.setPromptText("Escribe una palabra y pulsa Buscar");
        HBox.setHgrow(campoPalabra, Priority.ALWAYS);
        campoPalabra.setOnAction(evento -> buscar());

        botonBuscar = new Button("Buscar");
        botonBuscar.getStyleClass().add("boton-primario");
        botonBuscar.setDefaultButton(true);
        botonBuscar.setOnAction(evento -> buscar());

        indicadorDeProgreso = new ProgressIndicator();
        indicadorDeProgreso.setPrefSize(22, 22);
        indicadorDeProgreso.setVisible(false);

        HBox formulario = new HBox(10, selectorDeIdioma, campoPalabra, botonBuscar,
                indicadorDeProgreso);
        formulario.setAlignment(Pos.CENTER_LEFT);

        panelDeResultados = new VBox(12);
        panelDeResultados.getStyleClass().add("panel-resultados");

        ScrollPane zonaScroll = new ScrollPane(panelDeResultados);
        zonaScroll.setFitToWidth(true);
        zonaScroll.getStyleClass().add("zona-scroll");
        VBox.setVgrow(zonaScroll, Priority.ALWAYS);

        raiz = new VBox(18, cabecera, formulario, zonaScroll);
        raiz.setPadding(new Insets(24));
        raiz.getStyleClass().add("raiz");

        Scene escena = new Scene(raiz, 760, 580);
        escena.getStylesheets().add(
                getClass().getResource("/escritorio/estilos.css").toExternalForm());

        escenario.setTitle("Diccionarios — Acto 2");
        escenario.setScene(escena);
        escenario.show();

        cargarIdiomas();
    }

    // ------------------------------------------------------------------
    // Diálogo con la capa de negocio
    // ------------------------------------------------------------------

    /**
     * Pide al servidor la lista de idiomas disponibles para llenar el
     * desplegable.
     *
     * Esto en el Acto 1 era IMPOSIBLE: el contrato `InterfazDeUsuario` no tiene
     * ninguna forma de preguntar por los idiomas, porque no conoce la capa de
     * negocio. Por eso allí el idioma se escribía a mano y aquí se elige.
     */
    private void cargarIdiomas() {
        enSegundoPlano(
                suministradorDeDiccionarios::dameIdiomas,
                idiomas -> {
                    selectorDeIdioma.getItems().setAll(idiomas);
                    if (!idiomas.isEmpty()) {
                        selectorDeIdioma.setValue(idiomas.get(0));
                    }
                    mostrarMensajeInicial();
                },
                error -> pintarError("No se ha podido obtener la lista de idiomas del servidor.",
                        "Comprueba que el servicio web está arrancado:\n"
                                + "mvn -pl servicio-web spring-boot:run"));
    }

    private void buscar() {
        String idioma = selectorDeIdioma.getValue();
        String palabra = campoPalabra.getText() == null ? "" : campoPalabra.getText().trim();

        if (idioma == null || palabra.isEmpty()) {
            return;
        }

        pintarCargando(true);
        enSegundoPlano(
                () -> buscarEnElDiccionario(idioma, palabra),
                resultado -> {
                    pintarCargando(false);
                    pintarResultado(resultado);
                },
                error -> {
                    pintarCargando(false);
                    pintarError("El servicio de diccionarios no está disponible.",
                            "No se ha podido completar la búsqueda de \"" + palabra + "\".");
                });
    }

    /**
     * La lógica de una búsqueda, en cuatro líneas. Se ejecuta FUERA del hilo
     * gráfico: si se ejecutase dentro, la ventana se quedaría congelada
     * mientras el servidor calcula las palabras similares sobre 646.000
     * palabras. Ese "programa que no responde" que todos hemos sufrido es
     * justamente esto: alguien haciendo trabajo lento en el hilo de la interfaz.
     */
    private ResultadoDeBusqueda buscarEnElDiccionario(String idioma, String palabra) {
        Diccionario diccionario = dameElDiccionarioDe(idioma);

        Optional<List<String>> significados = diccionario.dameSignificados(palabra);
        if (significados.isPresent()) {
            return ResultadoDeBusqueda.encontrada(palabra, idioma, significados.get());
        }
        return ResultadoDeBusqueda.noEncontrada(palabra, idioma,
                diccionario.palabrasSimilares(palabra));
    }

    private Diccionario dameElDiccionarioDe(String idioma) {
        if (diccionarioEnUso != null && idioma.equals(idiomaDelDiccionarioEnUso)) {
            return diccionarioEnUso;
        }
        Diccionario diccionario = suministradorDeDiccionarios.dameDiccionarioDe(idioma)
                .orElseThrow(() -> new RuntimeException("No hay diccionario de " + idioma));
        diccionarioEnUso = diccionario;
        idiomaDelDiccionarioEnUso = idioma;
        return diccionario;
    }

    // ------------------------------------------------------------------
    // Pintado de resultados
    // ------------------------------------------------------------------

    private void mostrarMensajeInicial() {
        panelDeResultados.getChildren().clear();
        Label mensaje = new Label("Elige un idioma, escribe una palabra y pulsa Buscar.");
        mensaje.getStyleClass().add("mensaje-vacio");
        panelDeResultados.getChildren().add(mensaje);
    }

    private void pintarResultado(ResultadoDeBusqueda resultado) {
        panelDeResultados.getChildren().clear();

        if (resultado.encontrada()) {
            panelDeResultados.getChildren().add(tarjetaDeSignificados(resultado));
        } else {
            panelDeResultados.getChildren().add(tarjetaDeSugerencias(resultado));
        }
    }

    private VBox tarjetaDeSignificados(ResultadoDeBusqueda resultado) {
        Label palabra = new Label(resultado.palabra().toLowerCase());
        palabra.getStyleClass().add("palabra-encontrada");

        Label idioma = new Label(nombreDelIdioma(resultado.idioma()));
        idioma.getStyleClass().add("etiqueta-idioma");

        VBox tarjeta = new VBox(10, palabra, idioma);
        tarjeta.getStyleClass().add("tarjeta");

        for (String significado : resultado.significados()) {
            Label linea = new Label("— " + significado);
            linea.setWrapText(true);
            linea.getStyleClass().add("significado");
            tarjeta.getChildren().add(linea);
        }
        return tarjeta;
    }

    private VBox tarjetaDeSugerencias(ResultadoDeBusqueda resultado) {
        Label mensaje = new Label("No se ha encontrado «" + resultado.palabra().toLowerCase()
                + "» en " + nombreDelIdioma(resultado.idioma()) + ".");
        mensaje.setWrapText(true);
        mensaje.getStyleClass().add("palabra-no-encontrada");

        VBox tarjeta = new VBox(12, mensaje);
        tarjeta.getStyleClass().add("tarjeta");

        if (resultado.sugerencias().isEmpty()) {
            Label sinSugerencias = new Label("No hay sugerencias para esta palabra.");
            sinSugerencias.getStyleClass().add("ayuda");
            tarjeta.getChildren().add(sinSugerencias);
            return tarjeta;
        }

        Label ayuda = new Label("¿Quizás quisiste decir…?");
        ayuda.getStyleClass().add("ayuda");

        FlowPane sugerencias = new FlowPane(8, 8);
        for (String sugerencia : resultado.sugerencias()) {
            // Cada sugerencia es un botón: al pulsarlo se lanza una búsqueda
            // nueva. Esto en el Acto 1 era imposible: la aplicación ya había
            // terminado. Aquí la ventana está viva y el usuario manda.
            Button chip = new Button(sugerencia.toLowerCase());
            chip.getStyleClass().add("chip");
            chip.setOnAction(evento -> {
                campoPalabra.setText(sugerencia.toLowerCase());
                buscar();
            });
            sugerencias.getChildren().add(chip);
        }

        tarjeta.getChildren().addAll(ayuda, sugerencias);
        return tarjeta;
    }

    private void pintarError(String titulo, String detalle) {
        pintarCargando(false);
        panelDeResultados.getChildren().clear();

        Label cabecera = new Label("⚠️  " + titulo);
        cabecera.setWrapText(true);
        cabecera.getStyleClass().add("error-titulo");

        Label cuerpo = new Label(detalle);
        cuerpo.setWrapText(true);
        cuerpo.getStyleClass().add("error-detalle");

        VBox tarjeta = new VBox(8, cabecera, cuerpo);
        tarjeta.getStyleClass().addAll("tarjeta", "tarjeta-error");
        panelDeResultados.getChildren().add(tarjeta);
    }

    private void pintarCargando(boolean cargando) {
        indicadorDeProgreso.setVisible(cargando);
        botonBuscar.setDisable(cargando);
    }

    /**
     * El servidor entrega los códigos en mayúsculas ("ES", "EN"...) porque así
     * los normalizamos en el día 7. Aquí los traducimos a algo legible.
     */
    private String nombreDelIdioma(String codigo) {
        return switch (codigo.toUpperCase()) {
            case "ES" -> "🇪🇸  Español";
            case "ES.GRANDE" -> "🇪🇸  Español (ampliado)";
            case "EN" -> "🇬🇧  Inglés";
            case "ELFICO" -> "📖  Élfico";
            default -> "📖  " + codigo;
        };
    }

    // ------------------------------------------------------------------
    // Trabajo en segundo plano
    // ------------------------------------------------------------------

    /**
     * Ejecuta un trabajo lento fuera del hilo gráfico y devuelve el resultado
     * al hilo gráfico cuando termina.
     *
     * `Task` es la herramienta que da JavaFX para esto, y es un buen ejemplo
     * más de framework: nosotros sólo rellenamos el hueco `call()` y decimos
     * qué hacer al terminar; el orden de ejecución y el cambio de hilo los
     * gobierna JavaFX. Es el mismo Template Method que el CommandLineRunner de
     * Spring en el día 6.
     */
    private <T> void enSegundoPlano(ProveedorDeResultado<T> trabajo,
                                    java.util.function.Consumer<T> alTerminar,
                                    java.util.function.Consumer<Throwable> siFalla) {
        Task<T> tarea = new Task<>() {
            @Override
            protected T call() throws Exception {
                return trabajo.obtener();
            }
        };
        tarea.setOnSucceeded(evento -> alTerminar.accept(tarea.getValue()));
        tarea.setOnFailed(evento -> siFalla.accept(tarea.getException()));

        Thread hilo = new Thread(tarea, "busqueda-en-diccionarios");
        hilo.setDaemon(true);
        hilo.start();
    }

    @FunctionalInterface
    private interface ProveedorDeResultado<T> {
        T obtener() throws Exception;
    }
}
