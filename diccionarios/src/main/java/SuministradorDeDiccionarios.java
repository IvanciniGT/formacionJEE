import java.util.Optional;

public interface SuministradorDeDiccionarios {

    boolean tienesDiccionarioDe(String idioma);

    Optional<Diccionario> dameDiccionarioDe(String idioma);

}
