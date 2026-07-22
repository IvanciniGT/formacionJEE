import java.util.List;
import java.util.Optional;
import java.util.Map;

public class DiccionarioEnFichero implements Diccionario {

    private final String idioma;
    private final Map<String, List<String>> tablaDePalabrasYSignificados;

    public DiccionarioEnFichero(String idioma, Map<String, List<String>> tablaDePalabrasYSignificados) {
        this.idioma = idioma;
        this.tablaDePalabrasYSignificados = tablaDePalabrasYSignificados;
    }

    public String cualEsTuIdioma(){
        return idioma;
    }

    public boolean existe(String palabra){
        return tablaDePalabrasYSignificados.containsKey(palabra);
    }

    public Optional<List<String>> dameSignificados(String palabra){
        List<String> significados = tablaDePalabrasYSignificados.get(palabra);
        return Optional.ofNullable(significados);
            // si significados es nulo, devuelve un Optional vacío
            // si significados NO es nulo, devuelve un Optional con el valor de significados
    }
}
