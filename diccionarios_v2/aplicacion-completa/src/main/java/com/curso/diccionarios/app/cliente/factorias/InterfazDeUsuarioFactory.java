package com.curso.diccionarios.app.cliente.factorias;

import com.curso.diccionarios.ui.InterfazDeUsuario;
import com.curso.diccionarios.ui.consola.InterfazDeUsuarioConsola;
import com.curso.diccionarios.ui.escritorio.InterfazDeUsuarioEscritorio;

public class InterfazDeUsuarioFactory {

    public static InterfazDeUsuario dameInterfazDeUsuario(String[] args){
        // ------------------------------------------------------------------
        // ACTO 1: cambiar la interfaz de usuario de la aplicación ENTERA es
        // cambiar ESTA LÍNEA. Nada más.
        //
        // Ni Aplicacion, ni ui-api, ni el servidor, ni la base de datos, ni el
        // cliente HTTP se enteran de que ha pasado nada. Es la promesa que
        // hicimos el día 1 con el dibujo del README, cobrada nueve sesiones
        // después.
        //
        // Y es reversible en cinco segundos: se comenta una línea y se
        // descomenta la otra.
        // ------------------------------------------------------------------
        //return new InterfazDeUsuarioConsola(args);
        return new InterfazDeUsuarioEscritorio(args);
    }

}
