public class SuministradorDeDiccionariosFactory {
    
    public static SuministradorDeDiccionarios dameSuministradorDeDiccionarios(){
        return new SuministradorDeDiccionariosEnFicheros("diccionarios");
    }
}
