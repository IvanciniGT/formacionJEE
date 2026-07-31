package com.curso.diccionarios.app.cliente;

import javafx.application.Application;

/**
 * Punto de entrada real de la aplicación de escritorio.
 *
 * ============================================================================
 * ¿POR QUÉ EXISTE ESTA CLASE, SI PARECE NO HACER NADA?
 * ============================================================================
 *
 * Porque si intentamos arrancar directamente `AplicacionDeEscritorio` (que sí
 * extiende javafx.application.Application), Java se niega con este error:
 *
 *     Error: faltan los componentes de JavaFX runtime y son necesarios
 *            para ejecutar esta aplicación
 *
 * Y el error MIENTE: los componentes están ahí, los ha descargado Maven, están
 * en el classpath. Es de los errores más desconcertantes que uno se encuentra
 * con JavaFX, y aparece en miles de preguntas de StackOverflow.
 *
 * ----------------------------------------------------------------------------
 * QUÉ ESTÁ PASANDO DE VERDAD
 * ----------------------------------------------------------------------------
 *
 * En Java 9 se introdujo el SISTEMA DE MÓDULOS (JPMS). Desde entonces hay dos
 * formas de dar librerías a un programa:
 *
 *   - El CLASSPATH de toda la vida (lo que vimos en el día 3).
 *   - El MODULE-PATH, más moderno y más estricto.
 *
 * El lanzador de Java tiene una comprobación especial: si la clase que le pides
 * arrancar HEREDA de Application, exige que JavaFX venga como módulo con
 * nombre. Si viene por el classpath, se planta y saca ese mensaje.
 *
 * El truco, que es el que usa medio mundo, consiste en que la clase que arranca
 * NO herede de Application. Entonces la comprobación no se dispara, el programa
 * arranca con normalidad y desde dentro llamamos a `Application.launch()`
 * pasándole la clase de verdad.
 *
 * ----------------------------------------------------------------------------
 * LA LECCIÓN DE FONDO
 * ----------------------------------------------------------------------------
 *
 * Esto es una consecuencia directa de algo que ya conocéis: JavaFX SALIÓ del
 * JDK en Java 11, igual que CORBA y que JAX-WS (notas/temario-y-equivalencias.md).
 * Al dejar de venir "de serie", pasó a ser una librería más... pero el lanzador
 * de Java sigue teniendo dentro un trato especial para ella. Esa costura entre
 * lo viejo y lo nuevo es exactamente donde salen estos errores raros.
 *
 * Y de paso responde a algo que se pregunta mucho en clase: cuando una IA os
 * resuelva un error como éste en dos segundos, lo que os hace falta NO es la
 * solución, es saber por qué funciona. Si no, el día que el error cambie un
 * poco, estáis vendidos.
 */
public class LanzadorDeEscritorio {

    public static void main(String[] args) {
        Application.launch(AplicacionDeEscritorio.class, args);
    }
}
