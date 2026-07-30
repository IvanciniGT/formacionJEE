
# Paradigmas de programación

Es un nombre que los desarrolladores de software utilizan para referirse a una forma de usar un lenguaje para escribir código.

- Imperativo            La más tradicional. Escribir secuenas de comandos que el computador debe ejecutar.
- Procedural            Cuando el lenguaje nos permite agrupar secuencias de comandos bajo un nombre y reutilizarlas.
                            Creamos funciones, procedimientos, subrutinas, métodos.
- Orientado a objetos   Cuando el lenguaje me permite crear mis propios tipos de datos extendiendo los tipos
                            de datos que trae de serie.
- Funcional             Cuando el lenguaje me permite que una variable apunte a una función y posteriormente ejecutar 
                            esa función a través de la variable.

En JAVA 1.8 es cuando se añade el soporte de programación funcional.
// En esa versión se añaden 2 operadores nuevos a JAVA:
-   ->    Operador de flecha. Permite crear funciones anónimas.
-   ::    Operador de referencia a funciones.

Una vez añadido soporte para programación fuincional, se añade también un paquete nuevo: 
java.util.stream

    java.util.stream.Stream

Este paquete nos permite trabajar con un modelo de programación llamado MAP-REDUCE.
MAP REDUCE nos ayuda a procesar grandes cantidades de datos de manera paralela y distribuida.
Hacer operaciones sobre grandes cantidades de datos.

Y esto es justo lo que necesitamos para procesar las palabras de nuestro diccionario (650.000 palabras) y encontrar las que son similares (aplicando la distancia de Levenshtein) a una palabra que nos da el usuario.

En que consiste este modelo.
En java lo que hacemos es transformar una colección de datos de las que hemos venido manejando de forma tradicional (List, Set, Map) a un flujo de datos (Stream) y sobre ese flujo de datos podemos aplicar operaciones de transformación y filtrado.  

    Coleccion -> TRANSFORMACION1 -> 
                    Coleccion 2 -> TRANSFORMACION2 ->
                        Coleccion 3 -> ... -> TRANSFORMACION N -> 
                            Coleccion N -> REDUCCION -> Resultado final
                                            Reducción es convertir el objeto que tenemos de tipo Stream 
                                            A cualquier otra cosa que ya no sea un Stream. 
                                            Por ejemplo, un List, un Set, un Map, un String, un int, etc.

Como aplicar esto a nuestro escenario de las palabras similares.

    COLECCION INICIAL 
(todas las del diccionario)
    abanico
    acariciar
    dedicatoria
    manzana
    melón
    zanahoria
    ...

    vvvvv

Al final queremos un listado de las 10 palabras más similares a la que nos da el usuario.

Lo primero que podría hacer es FILTRAR de la colección inicial todas las palabras que tengan una longitud muy diferente a la que nos da el usuario (más de 3 caracteres).

Si tengo una palabra, puedo crear una funcion del tipo:

    boolean esLongitudSimilar(String palabra1, String palabra2) {      // Esto es un PREDICADO (Función que devuelve un booleano)
        return Math.abs(palabra1.length() - palabra2.length()) <= 3;
    }

A la colección 1 (todas las palabras) le puedo pedir que filtre los datos que cumplan con esa función.
Es decir, quiero que se aplique esa función a cada elemento de la colección y que me devuelva una nueva colección con los elementos para los que la función PREDICADO ha devuelto true.

    COLECCION 1 -> FILTRAR (esLongitudSimilar) -> COLECCION 2

Ahora tengo una colección que tiene solamente las palabras que tienen una longitud similar a la que nos da el usuario.
// Para ellas, calculo la distancia de Levenshtein y me quedo con las 10 más cercanas.

    interface PalabraPuntuada {
        String getPalabra();
        int getDistancia();
    }

    funcion PalabraPuntuada puntuarPalabra(String palabraUsuario, String palabraDiccionario) {
        int distancia = calcularDistanciaLevenshtein(palabraUsuario, palabraDiccionario);
        return new PalabraPuntuada(palabraDiccionario, distancia);
    }

Estamos pasando de una colección de palabras a una colección de objetos PalabraPuntuada, es decir, una colección que tiene las palabras junto con su puntuación (distancia de Levenshtein).

    COLECCION 2 ->  COLECCION 3
    palabra         palabra + puntuación

Necesito que se aplique esa función a cada elemento de la colección 2 y que me devuelva una nueva colección con los elementos devueltos por mi función de transformación. Esta operación es lo que en el mundo map-reduce se denomina un MAPEO. Aplicar una función a cada elemento de una colección y devolver una nueva colección con los resultados.

    COLECCION 2 -> MAP (puntuarPalabra) -> COLECCION 3
    palabra                                 palabra + puntuación

Lo siguiente que podríamos hacer es eliminar de la colección (FILTRO) las que tengan una distancia mayor o igual a 3.

    boolean esDistanciaAceptable(PalabraPuntuada palabraPuntuada) {
        return palabraPuntuada.getDistancia() < 3;
    }

    COLECCION 3 -> FILTRAR (esDistanciaAceptable) -> COLECCION 4
    palabra + puntuación                            palabra + puntuación (pero solo las que tienen distancia < 3)

El siguiente paso sería ordenar la colección 4 por distancia de menor a mayor.

    COLECCION 4 -> ORDENAR (por distancia) -> COLECCION 5
    palabra + puntuación                            palabra + puntuación (ordenadas por distancia)

El último paso sería eliminar las distancias... ya no las necesito.

    String eliminarDistancia(PalabraPuntuada palabraPuntuada) {
        return palabraPuntuada.getPalabra();
    }

    COLECCION 5 -> MAP (eliminarDistancia) -> COLECCION 6
    palabra + puntuación                        palabra  (cuya distancia era < 3 y ordenadas por distancia)

Por ultim, podría limitar a  las 10 primeras palabras de la colección 6.

    COLECCION 6 -> LIMITAR (10) -> COLECCION 7
    palabra                                      palabra (solo las 10 primeras)

Y YA TENEMOS NUESTRO OBJETIVO.

Partíamos de una gran colección de palabras y hemos ido aplicando operaciones de filtrado, mapeo (transformación) , ordenación y limitación hasta quedarnos con las 10 palabras más similares a la que nos da el usuario.

Hemos aplicado un modelo de programación funcional llamado MAP-REDUCE.
MAP-REDUCE son un conjunto CERRADO de operaciones que (debo aprender) podemos aplicar a una colección de datos para transformarla y reducirla hasta obtener un resultado final.

Operaciones típicas MAP-REDUCE:
- FILTRAR (filter)      -> Devuelve una nueva colección con los elementos que cumplen un PREDICADO.
- MAPEAR (map)          -> Devuelve una nueva colección con los elementos transformados por una función de transformación.
- ORDENAR (sorted)      -> Devuelve una nueva colección con los elementos ordenados según un comparador.
- LIMITAR (limit)       -> Devuelve una nueva colección con los primeros N elementos de la colección original.
- ...

Hay más de 50 operaciones.

Con esto podemos resolver nuestro problema de forma sencilla.

```java 

List<String> palabrasDelDiccionario = ...; // 650.000 palabras
String palabraObjetivo = ...; // palabra que nos da el usuario

List<String> palabrasSimilares = 
    palabrasDelDiccionario.stream()        // Convertir la Lista en un Objeto al que puedo aplicar operaciones de MAP-REDUCE
        .filter(   palabra -> Math.abs(palabra.length() - palabraObjetivo.length()) <= 3      ) // Mantengo las palabras cuya longitud es < 3
        .map(      palabra -> new PalabraPuntuada(palabra, calcularDistanciaLevenshtein(palabraObjetivo, palabra)) ) // Transformar cada palabra en un objeto PalabraPuntuada (palabra + distancia)
        .filter(palabraPuntuada -> palabraPuntuada.getDistancia() < 3) // MAntengo las palabras cuya distancia es < 3
        .sorted(Comparator.comparingInt(PalabraPuntuada::getDistancia)) // Ordenar por distancia de menor a mayor
        .map(   palabraPuntuada -> palabraPuntuada.getPalabra() )                                // Transformar cada objeto PalabraPuntuada en una String (eliminar la distancia)
        .limit(10)                                                       // Limitar a las 10 primeras palabras
        .collect(Collectors.toList());                                   // Convertir el Stream de vuelta a una List<String>

```

Esto, lo podríamos hacer con programación imperativa, pero serían páginas de código y sería más complejo de leer y mantener.
Además, se ejecutaría más lento.

Podemos hacer un programa completo que lea los ficheros de los diccionarios, cargue solo las palabras y aplique este procedimiento...
Como prueba de concepto.
Luego lo integramos en nuestro sistema.

```java
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BuscadorDePalabrasSimilares {

    private class PalabraPuntuada {
        private String palabra;
        private int distancia;

        public PalabraPuntuada(String palabra, int distancia) {
            this.palabra = palabra;
            this.distancia = distancia;
        }

        public String getPalabra() {
            return palabra;
        }

        public int getDistancia() {
            return distancia;
        }
    }

    public static void main(String[] args) {

        List<String> palabrasDelDiccionario = cargarPalabrasDelDiccionario();
        String palabraObjetivo = args[0];
        
        List<String> palabrasSimilares = 
            palabrasDelDiccionario.stream()
                .filter(   palabra -> Math.abs(palabra.length() - palabraObjetivo.length()) <= 3      )
                .map(      palabra -> new PalabraPuntuada(palabra, calcularDistanciaLevenshtein(palabraObjetivo, palabra)) )
                .filter(palabraPuntuada -> palabraPuntuada.getDistancia() < 3)
                .sorted(Comparator.comparingInt(PalabraPuntuada::getDistancia))
                .map(   palabraPuntuada -> palabraPuntuada.getPalabra() )
                .limit(10)
                .collect(Collectors.toList());
        
        System.out.println("Palabras similares a " + palabraObjetivo + ":");
        palabrasSimilares.forEach(System.out::println);
    }


    public static int calcularDistanciaLevenshtein(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        // i == 0
        int [] costs = new int [b.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            // j == 0; nw = lev(i - 1, j)
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }

    private static List<String> cargarPalabrasDelDiccionario() {
        // Leemos el fichero diccionario.txt de esta carpeta:
        File diccionarioFile = new File("./diccionario.txt");
        List<String> palabras = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(diccionarioFile))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                palabras.add(linea.split("=")[0].trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return palabras;
    }
}
```


2 segundos cada 10.000 palabras.
Si tenemos 650.000 palabras, tardará 130 segundos (2 minutos y 10 segundos) en procesarlas todas.
Realmente no es una locura... Dada una carga inicial.
El problema es que cada plaalabra que cargamos en la BBDD, cada insert, hace COMMIT.
Lo ideal sería evitar que se haga commit en cada palabra.
Un commit (una confirmacion de escritura en la BBDD) es una operación muy costosa.
Podríamos hacer un único commit al final de la carga de todas las palabras.

Sería hacer todos esos inserts en una transacción de BBDD y hacer commit al final.

Si estuvieramos trabajando con SQL:

```sql
BEGIN TRANSACTION;
INSERT INTO palabras (palabra) VALUES ('abanico');
INSERT INTO palabras (palabra) VALUES ('acariciar');
...
INSERT INTO palabras (palabra) VALUES ('zanahoria');
COMMIT;
```

Pero nosotros no estamos trabajando con SQL, estamos trabajando con JPA(JEE) y Hibernate.

Cómo podemos hacer esto? 
Es simple: Lo que necesitamos hacer es marcar la función desde la que estamos haciendo los inserts como transaccional. Spring/JPA nos permite hacer esto con la anotación @Transactional.


De 130 segundos hemos pasado a 65 segundos.
Cada 10k ahora tardan 1.2 segundos