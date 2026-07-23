import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Optional;

public class SuministradorDeDiccionariosEnFicheroTest {
    
    @Test
    @DisplayName("Ver que el suministrador tiene diccionario que existe en la carpeta")
    void suministradorTieneDiccionarioQueExisteTest() {
        // Contexto
        // En esa carpeta (que cogerá en automático la que hay en test/resources/diccionarios) hay un diccionario llamado "es.txt"
        SuministradorDeDiccionarios suministrador = SuministradorDeDiccionariosFactory.dameSuministradorDeDiccionarios();
        // Acción a Probar
        boolean tieneDiccionario = suministrador.tienesDiccionarioDe("es");
        // Verificación
        Assertions.assertTrue(tieneDiccionario);
    }
    @Test
    @DisplayName("Ver que el suministrador no tiene diccionario que no existe en la carpeta")
    void suministradorNoTieneDiccionarioQueNoExisteTest() {
        // Contexto
        // En esa carpeta (que cogerá en automático la que hay en test/resources/diccionarios) hay un diccionario llamado "es.txt"
        SuministradorDeDiccionarios suministrador = SuministradorDeDiccionariosFactory.dameSuministradorDeDiccionarios();
        // Acción a Probar
        boolean tieneDiccionario = suministrador.tienesDiccionarioDe("elfico");
        // Verificación
        Assertions.assertFalse(tieneDiccionario);
    }

    @Test
    @DisplayName("Obtener un diccionario que SÍ existe en la carpeta")
    void dameDiccionarioQueSiExisteTest() {
        // Contexto
        // En esa carpeta (que cogerá en automático la que hay en test/resources/diccionarios) hay un diccionario llamado "es.txt"
        SuministradorDeDiccionarios suministrador = SuministradorDeDiccionariosFactory.dameSuministradorDeDiccionarios();
        // Acción a Probar
        Optional<Diccionario> diccionario = suministrador.dameDiccionarioDe("es");
        // Verificación
        // Me devuelve un Optional con el diccionario dentro...
        Assertions.assertTrue(diccionario.isPresent());
        // ...y ese diccionario es el del idioma "es"
        Assertions.assertEquals("es", diccionario.get().cualEsTuIdioma());
    }

    @Test
    @DisplayName("No obtengo un diccionario que NO existe en la carpeta")
    void dameDiccionarioQueNoExisteTest() {
        // Contexto
        // En esa carpeta (que cogerá en automático la que hay en test/resources/diccionarios) hay un diccionario llamado "es.txt"
        SuministradorDeDiccionarios suministrador = SuministradorDeDiccionariosFactory.dameSuministradorDeDiccionarios();
        // Acción a Probar
        Optional<Diccionario> diccionario = suministrador.dameDiccionarioDe("elfico");
        // Verificación
        // Me devuelve un Optional vacío
        Assertions.assertTrue(diccionario.isEmpty());
    }
}
