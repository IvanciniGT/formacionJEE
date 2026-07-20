
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
1999 JAVA v1.3
2000 JAVA v1.4
2002 JAVA v1.5
2004 JAVA v1.6 \
2006 JAVA v1.7  > En 5 años, 2 versiones de JAVA
2009 JAVA v1.8 / 
2011 JAVA v1.9      En 8 años, 3 versiones de JAVA

En paralelo, en Java 1.8 -> 1.9 Oracle anuncia que va a cobrar por la JVM: 25$ / Año para particulares y 50$/Core para empresas.
En este momento, tras conversaciones principalmente con Google, Oracle convirtió la JVM en una ESPEFICACION ABIERTA, y cualquier empresa puede hacer su propia JVM. Hoy en día hay muchas implementaciones de la JVM:
- Oracle JVM
- OpenJDK
- Azul Zulu
- Amazon Corretto
- IBM J9
- ...

Además se lleva el lenguaje a una frecuencia de actualización de 6 meses, y se hace un cambio de nomenclatura en las versiones:
2011 JAVA v1.9 -> JAVA v9
2012 JAVA v10
2012 JAVA v11
2013 JAVA v12
2013 JAVA v13
2014 JAVA v14
2014 JAVA v15
2015 JAVA v16
2015 JAVA v17
2016 JAVA v18
2016 JAVA v19
2017 JAVA v20   
2018 JAVA v21
2019 JAVA v22
...
A punto de la 27

Gracias a estas conversaciones con Google, JAVA reflotó... Bueno, gracias a esas conversaciones y a un framework (coinjunto de librerías) llamado SPRING, que es un framework para hacer aplicaciones WEB en JAVA. Y gracias a eso, JAVA sigue vivo y con buena salud, auqneu hoy en día se usa en un nicho mucho más reducido que hace 20 año: Desarrollo de aplicaciones de BACKEND (trabajos en servidores de empresas)

Google NO PERDONO NI OLVIDO!
- Más adelante extrajo de su navegador CHROMIUM (La base del CHROME, del EDGE, OPERA, SAFARI...) el intérprete de JS... para que pudieran ejecutarse apps JS fuera de un navegador. Eso se llama NODEJS... y viene a ser el equivalente a la JVM de JAVA, pero para JS. 
Hoy en día JS es el lenguaje de programación MAS USADO DEL MUNDO junto con PYTHON.
- Encargó a una empresa (los mejores en el mundo haciendo entornos de desarrollo: JETBRAINS -> INTELLIJ) un nuevo lenguaje de programación. Se llamó KOTLIN... y kotlin se compila a BYTE-CODE, y se ejecuta sobre una máquina virtual de JAVA.

---

# Cómo ha ido evolucionando el mundo del desarrollo de software en empresas

Apps de escritorio -> App Web -> Arquitecturas de microservicios.