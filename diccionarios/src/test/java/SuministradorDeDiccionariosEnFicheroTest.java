import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

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
}
