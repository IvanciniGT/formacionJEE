public class InterfazDeUsuarioFactory {
    
    public static InterfazDeUsuario dameInterfazDeUsuario(String[] args){
        return new InterfazDeUsuarioConsola(args);
    }

}
