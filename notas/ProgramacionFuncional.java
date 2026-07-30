import java.util.function.*;
// En java 1.8 aparece en el API el paquete java.util.function,
// que contiene interfaces (TIPOS DE DATOS) funcionales (QUE PERMITEN APUNTAR A FUNCIONES)
// que se pueden usar para pasar variables/argumentos que apunten a funciones, y así poder invocar esas funciones desde las Variables/Argumentos.
// Hay 4 grandes tipos de interfaces funcionales en java:
// - Consumer<T>: Permite apuntar a funciones que reciben un argumento de tipo T y no devuelven nada (void).
//        Cualquier función de tipo setter es un ejemplo de función que puede apuntar a un Consumer<T>.
//        Para ejecutar la función desde una varible de tipo Consumer<T> se usa el método accept(T t) de la interfaz Consumer<T>.
// - Supplier<T>: Permite apuntar a funciones que no reciben argumentos y devuelven un valor de tipo T.
//        Cualquier función de tipo getter es un ejemplo de función que puede apuntar a un Supplier<T>.        
//        Para ejecutar la función desde una varible de tipo Supplier<T> se usa el método get() de la interfaz Supplier<T>.
// - Function<T,R>: Permite apuntar a funciones que reciben un argumento de tipo T y devuelven un valor de tipo R.
//        Para ejecutar la función desde una varible de tipo Function<T,R> se usa el método apply(T t) de la interfaz Function<T,R>.
// - Predicate<T>: Permite apuntar a funciones que reciben un argumento de tipo T y devuelven un valor booleano 
//        Funciones de tipo hasXXX? isXXX? son ejemplos de funciones que pueden apuntar a un Predicate<T>.
//        Para ejecutar la función desde una varible de tipo Predicate<T> se usa el método test(T t) de la interfaz Predicate<T>.

// En este paquete encontramos variantes de estas interfaces funcionales que permiten apuntar a funciones que reciben más de un argumento, 
// - BiConsumer<T,U>   función que recibe dos argumentos de tipo T y U y no devuelve nada (void).
// - BiFunction<T,U,R> función que recibe dos argumentos de tipo T y U y devuelve un valor de tipo R.
// - BiPredicate<T,U> función que recibe dos argumentos de tipo T y U y devuelve un valor booleano.

public class ProgramacionFuncional {
    
    public static double doblar(double numero) {
        return numero * 2;
    }

    public static double mitad(double numero) {
        return numero / 2;
    }

    public static void imprimirDoble(double numero) {
        double resultado = numero * 2;
        System.out.println(resultado);
    }

    public static void imprimirResultadoDeOperacion(double numero, Function<Double, Double> operacion) {
        double resultado = operacion.apply(numero);
        System.out.println(resultado);
    }

    public static void main(String[] args) {
        double numeral = 17;
        Consumer<Double> miVariable = ProgramacionFuncional::imprimirDoble;

        imprimirResultadoDeOperacion(5.,  ProgramacionFuncional::doblar);
        imprimirResultadoDeOperacion(10., ProgramacionFuncional::mitad);
        imprimirResultadoDeOperacion(5.,  numero -> numero + 4);

    }

}
