package com.curso.diccionarios.app.cliente.factorias;

import com.curso.diccionarios.ui.InterfazDeUsuario;
import com.curso.diccionarios.ui.consola.InterfazDeUsuarioConsola;

public class InterfazDeUsuarioFactory {
    
    public static InterfazDeUsuario dameInterfazDeUsuario(String[] args){
        return new InterfazDeUsuarioConsola(args);
    }

}
