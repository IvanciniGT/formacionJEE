package com.curso.diccionarios.servicioweb;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DiccionariosRestControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String procesarExcepcion(Exception e) {
        // Aquí podríamos hacer cosas como:
        // - Loguear el error en un fichero de log
        // - Enviar un email al administrador del sistema
        // - Enviar un mensaje a un sistema de monitorización
        // - etc...
        return e.getMessage();
    }
}
// Esto está acabado!