public class SuministradorDeDiccionariosFactory {

    public static SuministradorDeDiccionarios dameSuministradorDeDiccionarios(){
        // Vamos a pedir a java que busque una carpeta llamada "diccionarios"
        // en el classpath de la aplicación:
        String carpetaDeDiccionarios = SuministradorDeDiccionariosFactory.class
                                                                        .getClassLoader() // Dame el cargador de clases (el que ha leido esta clase del disco duro)
                                                                        .getResource("diccionarios") // Busca una carpeta en esa misma ruta llamada "diccionarios"
                                                                        .getPath(); // Y dame la ruta como un texto.
        // El archivo SuministradorDeDiccionarios.java no está en la misma carpeta que los diccionarios.
        // Pero el compilado (SuministradorDeDiccionarios.class) sí está en la misma carpeta que los diccionarios.
        // Y esa es la clase que se carga! No el .java, sino el .class. Y ese .class sí está en la misma carpeta que los diccionarios.
        return new SuministradorDeDiccionariosEnFicheros(carpetaDeDiccionarios);
    }
}
