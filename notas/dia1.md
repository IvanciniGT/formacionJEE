
# JAVA

Lenguaje de programación, con ciertas características:

## Tipado dinámico (débil) vs tipado estático (fuerte).

Cualquier lenguaje de programación permite manipular DATOS. Y en todo lenguaje de programación, los datos pueden ser de distinto tipo: NUMEROS, TEXTOS, FECHAS...
TODO LENGUAJE DE PROGRAMACION tiene distintos tipos de datos con los que podemos trabajar.

El concepto de variable CAMBIA de lenguaje en lenguaje.
En JAVA, JS, TS, PYTHON una variable NO ES un contenedor (Espacio de memoria) donde almaceno un valor para su posterior recuperación. Esa definición puede aplicar a otros lenguajes: C, C++, PASCAL, COBOL, FORTRAN, etc.

```java
String texto = "Hola";
texto = 4; // Esto daría error de sintaxis en tiempo de compilación
```
```python
texto = "Hola"
texto = 4
```

Esa linea se ejecuta en partes:
1.  "Hola"       Crear en memoria un objeto (dato) de tipo String con valor "Hola"
                 La memoria RAM es como un cuaderno de cuadrícula... donde hemos abierto por algún sitio y escreibimos por ahí la palabra "Hola"
2. String texto  Crea una variable con el nombre texto, que puede apuntar a datos de tipo texto(String)
                 Una variable es como un postit (Marcador) en el que escribimos la palabra "texto".
                 En el caso de JAVA (TS, C), los postits son de distintos COLORES según el tipo de dato al que pueden apuntar. En este caso, el postit es de color AZUL (String).
                 AMARILLOS (Integer), VERDES (Boolean), ROJOS (Float), etc.
                    Hay lenguajes donde los postits son todos iguales (JS/PYTHON). 
                    Es decir, en los que una variable puede apuntar/guardar a CUALQUIER TIPO DE DATO.
                    Esos lenguajes se denominan de tipado dinámico o débil.

                    Por otro lado tenemos lenguajes en los que los postits son de distintos colores según el tipo de dato al que pueden apuntar. 
                    Es decir, en los que la VARIABLES TIENEN UN TIPO DE DATO (Igual que los DATOS, que también tienen un tipo de dato).
                    Estos lenguajes se denominan de tipado estático o fuerte.

                    El tipado estático o fuerte ESTA CONSIDERADO UNA VENTAJA.
                    El tipado dinámico o débil ESTA CONSIDERADO UNA DESVENTAJA.
3. =             Y ese postit lo PEGO al lado del dato que hemos creado en memoria (el objeto "Hola")

El tipado débil o dinámico es MUY PROBLEMATICO, pero cómodo... tengo que escribir menos.
Si quiero hacer un porograma muy simple, posiblemente me convenga un lenguaje de tipado débil o dinámico. 

En cuanto quiero hacer un programa más complejo o donde hay más personas involucradas, me conviene un lenguaje de tipado fuerte o estático.

### Ejemplo de tipado débil o dinámico:

    funcion generarInforme(titulo, datos){
        // código
    }

### Ejemplo de tipado fuerte o estático:

    funcion PDFDocument generarInforme(String titulo, List<Integer> datos){
        // código
    }

## JAVA, PY, JS, TS son lenguajes en los que se hace una gestión automática de la memoria RAM.

```java
String texto = "Hola";
texto = "adios";
```
1. "adios"      Crear en memoria un objeto (dato) de tipo String con valor "adios"  
                Abre el cuaderno y escribe en él la palabra "adios". 
                Dónde? En el mismo sitio donde estaba escrito "hola" o en otro? EN OTRO.
                En JAVA EN OTRO.
                Si fuera C, C++, PASCAL, COBOL, FORTRAN, etc. escribiría en el mismo sitio donde estaba escrito "hola".
2. texto =      Se quita (arranca) el postit del lado del dato "Hola" y se pega al lado del dato "adios". VARIAMOS la UBICACION de la VARIABLE.
                  En este momento del tiempo, cuántos datos de tipo String hay en RAM? 2
                  "Hola" y "adios".
                  Si el programa lo hiciera en C, C++, PASCAL, COBOL, FORTRAN, etc. habría 1 solo dato de tipo String en RAM. "adios".

                  Por hacer el programa en JAVA, voy a necesitar mucha más memoria RAM que si lo hiciera en C, C++, PASCAL, COBOL, FORTRAN, etc.
                  JAVA HACE UN ABUSO DE LA MEMORIA RAM... Genera mucho DESPERDICIO (BASURA = GARBAGE)

                  Como al dato "Hola" ya no apunta ninguna variable, el dato "Hola" queda marcado como BASURA (GARBAGE) en la memoria RAM. Y quizás (o quizás no... tenemos poco control de ello) entre en algún momento el GARBAGE COLLECTOR (un proceso que se ejecuta en paralelo con nuestro programa) y lo borre de la memoria RAM. 

                  Cuando crean JAVA se crea INTENCIONALMENTE para hacer un uso ineficiente de la RAM.
                  En C, C++ el desarrollador necesita invertir muchas más horas en el desarrollo... y el desarrollo es más complejo. Necesita reservar espacio en memoria para cualquier dato que quiera guardar... Necesita LIBERAR EXPLICITAMENTE espacio de memoria cuando ya no lo necesita. Y si no lo hace, el programa se puede quedar sin memoria (memory leak) y detenerse. En JAVA (y JS, TS, PYTHON) no hace falta que el desarrollador se preocupe de eso. El GARBAGE COLLECTOR se encarga de liberar la memoria RAM que ya no se necesita.

                  Como consecuencia, el mismo desarrollo:
                    - JAVA: 300 horas de desarrollador (50€/hora) = 15.000€ + 1500€ de RAM
                    - C, C++: 400 horas de desarrollador (60€/hora) = 24.000€

## Compilados vs interpretados

Compilar es traducir de un lenguaje de un cierto nivel de abstracción a otro lenguaje de un nivel de abstracción inferior. Por ejemplo, de un lenguaje de programación a lenguaje máquina.

Las computadoras no hablan JAVA, ni PYTHON, ni JS, ni TS, ni C, ni C++, ni PASCAL, ni COBOL, ni FORTRAN... hablan LENGUAJE MÁQUINA (que va influenciado por el Sistema Operativo y el procesador que tenga la computadora).

Yo hago programas en un lenguaje de programación... pero, tengo que llevarlo al lenguaje máquina para que la computadora pueda ejecutarlo.

Hay 2 formas de traducir de MI LENGUAJE de prorgamación a LENGUAJE MÁQUINA:

- Compilación: Es lo mismo que si TRADUZCO UN LIBRO Antes de distribuirlo
   C -> Compilador -> LENGUAJE MÁQUINA Windows x86
                      LENGUAJE MÁQUINA Linux x86 
                      LENGUAJE MÁQUINA Linux arm64
                      ...
   JAVA -> Compilador -> BYTE-CODE (Lenguaje de bajo nivel, pero no lenguaje máquina)

- Interpretación: Es lo mismo que si TRADUZCO UN LIBRO Mientras se lee (con ayuda de un intérprete = PROGRAMA QUE "compilar" en tiempo real)
   PYTHON -> Intérprete -> LENGUAJE MÁQUINA Windows x86
              cython       LENGUAJE MÁQUINA Linux x86 
                           LENGUAJE MÁQUINA Linux arm64
                          ...
   BYTE-CODE -> Intérprete  -> LENGUAJE MÁQUINA Windows x86
                 JVM           LENGUAJE MÁQUINA Linux x86 
                               LENGUAJE MÁQUINA Linux arm64
                          ...

Cuando instalamos JAVA lo que realmente estamos instalando es la JVM (Java Virtual Machine) que es un intérprete de BYTE-CODE. La JVM es un programa que se ejecuta en el sistema operativo y que traduce el BYTE-CODE a LENGUAJE MÁQUINA.

            compilador de JAVA          intérprete de BYTE-CODE = JVM
              vvv                            vvv
    .java -> javac -> .class (BYTE-CODE) -> java -> LENGUAJE MÁQUINA

---

Máquina virtual que ejecuta código escrito en el leguaje de programción BYTE-CODE

---

# Qué tal le va a JAVA como lenguaje de programación?

La realidad es que a JAVA le iba mejor que le va.

JAVA sale a finales de los 90. Lo crea una empresa llamada Sun Microsystems.
cuando sale, la gente se vuelve loca con JAVA... El lenguaje del FUTURO lo llamaban. 
Todo el mundo quería aprender JAVA.
Y TODO SE HACIA EN JAVA:

Escenario en 2000-2005
- App de escritorio: JAVA
- App de software embebido: JAVA (J2ME)
- App WEB: JAVA (JSP, Servlets)
- App Mobile: JAVA (Android)

Escenario en 2026
- App de escritorio: JS, VB
- App de software embebido: PYTHON, C, C++
- App WEB: 
  - Backend : JAVA + SPRING
  - Frontal : JS, TS
- App Mobile: Android -> KOTLIN

JAVA hoy en día, su UNICO NICHO es el BACKEND de aplicaciones WEB. Y para eso, JAVA es un lenguaje de programación muy bueno, sobre todo gracias al framework SPRING. Pero para todo lo demás, JAVA es un lenguaje de programación que ha quedado obsoleto.

## Qué ha pasado en estos 20 años?

ORACLE!

Oracle en 2009 compra SUN MICROSYSTEMS y con ello JAVA. Y a partir de ahí, JAVA empieza a decaer en picado.

Oracle (empresa gigante que hace la MEJOR BBDD RELACIONAL DEL MERCADO) es en paralelo el mayor destructor de software del mundo. En manos de Oracle han caido mucho programas, que han sido destruidos, abandonados o han quedado obsoletos. Entre ellos, JAVA.

    - MySQL -> MariaDB
    - OpenOffice -> LibreOffice
    - A JAVA le pasó lo mismo.

## Historial de versiones de JAVA

1996 JAVA v1.0
1997 JAVA v1.1
1998 JAVA v1.2
2000 JAVA v1.3
2002 JAVA v1.4
2005 JAVA v1.5
2006 JAVA v1.6 \
2011 JAVA v1.7  > En 8 años, 2 versiones de JAVA
2014 JAVA v1.8 / 
2017 JAVA v1.9    En 11 años, 3 versiones de JAVA

En paralelo, en Java 1.8 -> 1.9 Oracle anuncia que va a cobrar por la JVM: 25$ / Año para particulares y 50$/Core para empresas.
En este momento, tras conversaciones principalmente con Google, Oracle convirtió la JVM en una ESPEFICACION ABIERTA, y cualquier empresa puede hacer su propia JVM. Hoy en día hay muchas implementaciones de la JVM:
- Oracle JVM
- OpenJDK
- Azul Zulu
- Amazon Corretto
- IBM J9
- ...

Además se lleva el lenguaje a una frecuencia de actualización de 6 meses, y se hace un cambio de nomenclatura en las versiones:
2017 JAVA v1.9 -> JAVA v9
2017 JAVA v10
2018 JAVA v11
2018 JAVA v12
2019 JAVA v13
2019 JAVA v14
2020 JAVA v15
2020 JAVA v16
2021 JAVA v17
2021 JAVA v18
2022 JAVA v19
2023 JAVA v20   
2024 JAVA v21
2025 JAVA v22

...
A punto de la 27

Gracias a estas conversaciones con Google, JAVA reflotó... Bueno, gracias a esas conversaciones y a un framework (coinjunto de librerías) llamado SPRING, que es un framework para hacer aplicaciones WEB en JAVA. Y gracias a eso, JAVA sigue vivo y con buena salud, auqneu hoy en día se usa en un nicho mucho más reducido que hace 20 año: Desarrollo de aplicaciones de BACKEND (trabajos en servidores de empresas)

Google NO PERDONO NI OLVIDO!
- Más adelante extrajo de su navegador CHROMIUM (La base del CHROME, del EDGE, OPERA, SAFARI...) el intérprete de JS... para que pudieran ejecutarse apps JS fuera de un navegador. Eso se llama NODEJS... y viene a ser el equivalente a la JVM de JAVA, pero para JS. 
Hoy en día JS es el lenguaje de programación MAS USADO DEL MUNDO junto con PYTHON.
- Encargó a una empresa (los mejores en el mundo haciendo entornos de desarrollo: JETBRAINS -> INTELLIJ) un nuevo lenguaje de programación. Se llamó KOTLIN... y kotlin se compila a BYTE-CODE, y se ejecuta sobre una máquina virtual de JAVA.

---

# Cómo ha ido evolucionando el mundo del desarrollo de software en empresas

Apps de escritorio / que corrían en 1 ordenador
- El problema de estas aplicaciones era compartir datos.
  
Con la llegada de las redes, comenzamos a crear apps con una arquitectura: cliente-servidor
- Parte de la app se ejecuta en el cliente
- Otra parte de la app se ejecuta en el servidor


    APP CLIENTE <-------------> APP SERVIDOR
                  Protocolo (conjunto de normas para que la comunicación se pueda realizar)
                  Lenguaje de comunicación (lenguaje que entienden tanto el cliente como el servidor)


    EMISOR -> MENSAJE (idioma = HTML) -> RECEPTOR (Canal = INTERNET)
                Protocolo = HTTP


Los datos quedan en el servidor, y el cliente se conecta al servidor para acceder a los datos.
Cuando empezamos con esto, en los servidores lo que teniíamos eran las BBDD, y en las máquinas cliente montábamos toda la aplicación que se encargaba de la gestión de los datos. Eran aplicaciones complejas.

Poco a poco, parte de la lógica de la gestión de los datos se fue trasladando al servidor, y en el cliente se fue dejando solo la parte representar los datos.

Esto empezó a funcionar bien (el llevar trabajos más pesados a los sevridores).

En paralelo con esto, explotó el mundo WEB.
WEB != INTERNET

INTERNET = Conjunto descentralizado de redes interconectadas que usan un protocolo de comunicación común (TCP/IP) para comunicarse entre sí.
Me permite conectar máquinas muy alejadas entre sí, y que estén en distintos países, continentes, etc.

WEB = Un servicio que se ofrece sobre INTERNET. La WEB es un servicio que permite acceder a documentos HTML que están en servidores WEB, y que se pueden ver desde un navegador WEB.
Es uno de los muchos servicios que se ofrecen sobre INTERNET: EMAIL, VOIP, FTP, SSH, etc.

La WEB la crea Tim Berners-Lee en 1990, y en 1993 se hace pública. Lo que define son: HTML + HTTP

    HTML = HyperText Markup Language = Lenguaje de marcado de hipertexto (documentos con enlaces)
    HTTP = HyperText Transfer Protocol = Protocolo de transferencia de hipertexto (protocolo de comunicación entre cliente y servidor)

En sus orígenes, la WEB funcionaba mediante conjuntos de documentos HTML que estaban en servidores WEB, y que se podían ver desde un navegador WEB. Esos documentos HTML eran estáticos, es decir, no cambiaban. 

Con el tiempo empezamos a querer que esos documentos se pudieran generar bajo demanda: Y EMPEZAMOS A HABLAR DE APLCIACIONES WEB.

    Yo quiero acceder a una web.. y ver MI listado de pedidos, facturas, expedientes...
    Esas páginas web había que generarlas bajo demanda, con datos actualizados de una BBDD.

    Para eso necesitamos un lenguaje de programación que se ejecute en el servidor, y que genere documentos HTML bajo demanda. 

    Cliente                     Servidor
    (NAVEGADOR) -------------->
                 http request    Servidores APP (Apache Tomcat, WAS, WebLogic, JBOSS) -> Programa <--> BBDD
                <-------------
                 http response 
                    (HTML) 

Este modelo funcionó durante muchos años. Hoy en día, está obsoleto. ¿Por qué?

Los tiempos han cambiado. Y hoy en día, los navegadores de Internet NO SON LA FORMA PRINCIPAL DE ACCEDER A DATOS, GESTIONES...

Hoy en día hay muchos frontales de usuario:
- App Android
- App iOS
- Navegador WEB
- Smart TV
- Asistentes de voz (siri, ok google, alexa)
- IVR (Interactive Voice Response) -> Sistemas de atención al cliente por teléfono

HTML Es un lenguaje orientado a la creación de DOCUMENTOS con una repreentación VISUAL:
- El titulo con fuente arial 12 pt.. en negrita
- Lista con un margen a la izquierda de 20px
- Imagen con un tamaño de 200px x 300px

Eso NO SIRVE para una app Android, ni para una app iOS, ni para un asistente de voz, ni para un IVR.

HTML Deja de ser el lenguaje natural para la transmisión de datos. En la industria comenzamos a buscar otros lenguajes que nos permitan transmitir datos de forma más natural, y que no estén orientados a la representación visual de los datos (XML -> JSON)
Antiguamente las aplicaciones que se ejecutaban en los servidores GENERABAN HTML para mandarlo a un cliente.
Hoy en día, las aplciaciones que se ejecutan en los servidores GENERAN DATOS (JSON) para mandarlos a un cliente. 
Si quiero montar una aplicación WEB accesible desde un navegador, lo que hacemos es crear luego programas en JavaScript que se ejecutan en el navegador y que interpretan esos datos (JSON) y generen dentro del propio cliente/navegador HTML.

    CLIENTES                               SERVIDOR
    Navegador (JS -> HTML) ---> HTTP --->   Servidor APP (Apache Tomcat) -> App (JAVA) -> BBDDs
    Android App            <--- JSON ----
    iOS App
    IVR
    Asistente de voz
        ( JSON-> AUDIO)

En paralelo con esto, hace 20-15-10 años, tendíamos a montar en las empresas sistemas MUY GRANDES = MONOLITOS (arquitecturas monolíticas). Queríamos una aplicación / sistema que hiciera DE TODO:

    Animalitos Fermín
        - Venta de productos para animales de compañía
        - Citas veterinarías
        - Peluqueros
        - Empleados (vacaciones, nóminas)

Estos sistemas tan grandes con el tiempo nos hemos dado cuenta que son muy dificiles de mantener. 
Cualquier cambio que hacemos en ellos puede tener impacto en cualquier otra parte del sistema.

Con el tiempo hemos aprendido a crear sistemas de otra forma: ARQUITECTURAS DE COMPONENTES DESACOPLADOS.
Esta forma de trabajo, donde en lugar de hacer un sistema muy grande, lo que hacemos es muchos sistemas muy pequeños, comunicados entre si, dan lugar a aplicaciones MAS SENCILLAS DE MANTENER... y eso es la clave!

---

# Nuestra primera aplicación

Queremos un programa que podamos ejecutar desde una terminal de comandos:

    # Buscar una palabra que existe en el diccionario solicitado
    c:\> buscarPalabra "melón" "es"
    La palabra melón existe en el diccionario de español, y significa:
    - Fruto del melonero
    - Persona con pocas luces: "Eres un melón!"

    # Buscar una palabra que no existe en el diccionario solicitado
    c:\> buscarPalabra "melón" "en"
    La palabra no melón existe en el diccionario de inglés.

    # Buscar una palabra en un diccionario desconocido
    c:\> buscarPalabra "melón" "elfos"
    Lo siento, pero no tengo diccionario élfico.

La solución más simple sería montar un único proyecto con 2 diccionarios.
PERO NO BUSCAMOS LA SOLUCION MAS SIMPLE. BUSCAMOS LA SOLUCION MAS MANTENIBLE!
Teniendo en cuenta que el programa que hagamos va a evolucionar en el futuro.

- Voy a tener una parte del programa que se encargue de la gestión de DICCIONARIOS/PALABRAS/SIGNIFICADOS
- Voy a tener otra parte del programa que se encargue de la gestión de la INTERFAZ DE USUARIO (terminal, navegador, app Android, app iOS, asistente de voz...)
- Cada diccionario va a ser un proyecto independiente, que se pueda añadir o quitar del programa principal sin afectar al resto del programa.
  Porque además, debe tener su propio ciclo de vida = CONTROL DE VERSIONES


  App de diccionarios v2.0.0
  Diccionario de Español v1.0.0 (10.000 palabras)
  Diccionario de Inglés v2.1.0  (10.000 palabras)

 


Este programa donde va a guardar los diccionarios, con las palabras y las definiciones?
- Quizás hoy en día en ficheros de texto...pero el día de mañana podría ser en una BBDD
Hoy en día, el programa se usa desde una terminal de comandos... pero el día de mañana podría ser desde un navegador WEB, o desde una app Android, o desde un asistente de voz. O desde todos ellos!
Hoy en día, tengo diccionario de Español y de Inglés... Pero verás que me piden en unos meses el de Alemán, chino...


> Un producto de software POR DEFINICION es un producto sujeto a cambios y mantenimiento.

> Un coche POR DEFINICION es un producto sujeto a mantenimiento.
Al año voy al taller... CAMBIO DE ACEITE!

Para una empresa en problema es lo que llamamos el COSTO DE CICLO DE VIDA (LCC = Life Cycle Cost). Es el coste de tener un producto durante su ciclo de vida.

```java
public interface SuministradorDeDiccionarios {
    public boolean tienesDiccionarioDe(String idioma);
    public Optional<Diccionario> getDiccionario(String idioma);
}

public interface Diccionario {
    public String getIdioma();
    public boolean existe(String palabra);
    public Optional<List<String>> getSignificados(String palabra);
    // palabra: "melón" -> ["Fruto del melonero", "Persona con pocas luces: 'Eres un melón!'"]
    // palabra: "archilococo", que devuelve?
    // - Lista vacia
    // - null
    // - Generase una excepción: NoSuchWordException
    // Un Optional (Java 1.8) es como una caja. Siempre me dan la caja.,.. pero puede venir rellena o no! A un optional le puedo preguntar: .isEmpty() .isPresent() .get()
    // Desde JAVA 1.8 es una MUY MALA PRACTICA QUE UNA FUNCION DEVUELVA NULL. Mejor que devuelva un Optional vacio.
}
public class SuministradorDeDiccionariosDesdeFichero implements SuministradorDeDiccionarios {
    public SuministradorDeDiccionariosDesdeFichero(String rutaDirectorioDiccionarios){
        //...
    }
  // Implementación de la interfaz SuministradorDeDiccionarios que lee los datos de un fichero de texto
}
public class DiccionarioDesdeFichero implements Diccionario {
    public DiccionarioDesdeFichero(String rutaFichero){
        //...
    }
  // Implementación de la interfaz Diccionario que lee los datos de un fichero de texto
}  
public class DiccionarioEnBBDD implements Diccionario {
    public DiccionarioDesdeFichero(String cadenaConexionABBDD, String usuario, String password){
        //...
    }
  // Implementación de la interfaz Diccionario que lee los datos de un fichero de texto
}  





```
```java

//C:\> java AppDiccionarios "melón" "es"

public class AppDiccionarios{
    public static void main(String[] args){
        String palabra = args[0];
        String idioma = args[1];
        ProcesadorDePeticiones procesador = new ProcesadorDePeticiones();
        procesador.procesarPeticion(idioma, palabra);
    }
}


public class ProcesadorDePeticiones{

    public void procesarPeticion(String idioma, String palabra) {
        SuministradorDeDiccionarios suministrador;
        if(suministrador.tienesDiccionarioDe(idioma)){
            Diccionario miDiccionaro = suministrador.getDiccionario(idioma);
            if (miDiccionaro.existe(palabra)){
                List<String> significados = miDiccionaro.getSignificados(palabra);
                System.out.println("La palabra " + palabra + " existe en el diccionario de " + idioma + ", y significa:");
                for (String significado : significados){
                    System.out.println("- " + significado);
                }
            } else {
                System.out.println("La palabra " + palabra + " no existe en el diccionario de " + idioma + ".");
            }
        }else{
            System.out.println("Lo siento, pero no tengo diccionario " + idioma + ".");
        }
    }
}

public interface SuministradorDeDiccionarios{
    public boolean tienesDiccionarioDe(String idioma);
    public Diccionario getDiccionario(String idioma);
}

public interface Diccionario{
    public boolean existe(String palabra);
    public List<String> getSignificados(String palabra);
}







public class SuministradorDeDiccionariosDesdeBBDD implements SuministradorDeDiccionarios{
    public SuministradorDeDiccionariosDesdeBBDD(String rutaDirectorioDiccionarios){
        //...
    }
    public boolean tienesDiccionarioDe(String idioma){
        //...
    }
    public Diccionario getDiccionario(String idioma){
        //...
    }
}
public class DiccionarioDesdeBBDD implements Diccionario{
    public DiccionarioDesdeBBDD(String rutaFichero){
        //...
    }
    public boolean existe(String palabra){
        //...
    }
    public List<String> getSignificados(String palabra){
        //...
    }
}
```

Hay un programador muy famoso llamado Edgar Dijkstra. En el año 1971 ganó un premio llamado TURING AWARD. Es el premio más importante que puede recibir un programador. En su discurso de aceptación del premio, dijo algo que se ha hecho muy famoso:
THE HUMBLE PROGRAMMER.
De ese artíkculo salió uno de los principios más famosos del mundo del desarrollo de software: SOC: Separation of Concerns = Separación de preocupaciones. Cuando hago un trozo de un programa, me ocupo de ese trozo del programa... y me olvido de lo demás... ESE SERA OTRO PROBLEMA!



```txt
amanecer=Salir el sol
manzana=Fruto del manzano
melón=Fruto del melonero|Persona con pocas luces: "Eres un melón!"
zapato=Calzado que cubre el pie
```





---

---

# Versionado SEMANTICO

    vA.B.C

                ¿Cuándo suben?
    A   MAJOR   Cuando se quita una funcionalidad (BREAKING CHANGE): Cambio que rompe compatibilidad
    B   MINOR   Cuando metemos nueva funcionalidad
                O cuando marcamos una funcionalidad como obsoleta (deprecated)
    C   PATCH   Si se arreglan BUGS/defectos del producto

