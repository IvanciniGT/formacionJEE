package com.curso.diccionarios.app.cliente;

import com.curso.diccionarios.api.SuministradorDeDiccionarios;
import com.curso.diccionarios.app.cliente.factorias.SuministradorDeDiccionariosFactory;
import com.curso.diccionarios.ui.escritorio.VentanaDeDiccionarios;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * ============================================================================
 * ACTO 2 — Punto de arranque de la aplicación de escritorio.
 * ============================================================================
 *
 * Comparad esta clase con `Aplicacion` (la de al lado) y ahí está toda la
 * lección de la sesión:
 *
 * `Aplicacion` es un GUION: pide la palabra, pide el idioma, pregunta al
 * negocio, manda pintar y termina. El orden lo decidimos nosotros.
 *
 * Esta clase no tiene guion. Sólo dice "monta la ventana con este suministrador
 * de diccionarios" y se aparta. A partir de ahí el orden lo decide el usuario
 * pulsando cosas, y quien llama a nuestro código es JavaFX.
 *
 * Eso es INVERSIÓN DE CONTROL, y es la misma idea que ya visteis en el día 4
 * con Spring: `SpringApplication.run(...)` es a `ServicioWeb` lo que
 * `launch(args)` es a esta clase. Una línea, y el framework toma el mando.
 *
 * ----------------------------------------------------------------------------
 * ¿Y por qué esta clase vive aquí, en aplicacion-completa, y no en el módulo
 * ui-escritorio?
 *
 * Porque es la única que toma una DECISIÓN: de dónde salen los diccionarios.
 * Y ese es el trabajo de la factoría, que vive en este módulo. La ventana
 * (VentanaDeDiccionarios) no debe saberlo, y por eso lo recibe por el
 * constructor en lugar de construirlo.
 *
 * La prueba: hoy la factoría devuelve un suministrador que habla por HTTP con
 * el servidor. Si mañana descomentáis la otra línea de
 * SuministradorDeDiccionariosFactory, esta aplicación de escritorio leería de
 * ficheros .txt sin conexión a red, y NO habría que tocar ni una línea de la
 * ventana.
 */
public class AplicacionDeEscritorio extends Application {

    @Override
    public void start(Stage escenarioPrincipal) {
        // La MISMA factoría que usa la aplicación de consola. No hay una
        // versión "de escritorio" del suministrador de diccionarios: hay UNO,
        // y lo comparten los dos clientes.
        SuministradorDeDiccionarios suministradorDeDiccionarios =
                SuministradorDeDiccionariosFactory.dameSuministradorDeDiccionarios();

        new VentanaDeDiccionarios(suministradorDeDiccionarios).mostrarEn(escenarioPrincipal);
    }

    // OJO: esta clase NO tiene método main. El arranque está en
    // LanzadorDeEscritorio, y el motivo está explicado allí. Es uno de los
    // tropiezos más famosos de JavaFX y merece la pena entenderlo.
}
