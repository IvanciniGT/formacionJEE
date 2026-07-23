# Usos de la memoria RAM

Los procesos (programas en ejecución) usan la memoria RAM para muchas cosas:
- Para almacenar el código del programa.
- Mantienen punteros a las lineas de código que se están ejecutando en cada momento. (Stack de ejecución)
- Guardar datos de trabajo (variables)
- Cache (almacena datos temporalmente para un acceso rápido)
  - OJO CON LA CACHE!
  - Es importante saber montar una cache!

        La cache sirve para ir almacenando datos que cuesta mucho más tiempo ir a buscarlos a su origen. PERO... u sistema debería poder trabajar sin la cache.
        Imaginad en nuestro sistema que tengo 50 diccionarios...
        Y los tengo en RAM, cacheados... pero ya no hay espacio en RAM
        Y de repente me piden que cargue un nuevo diccionario... y no hay espacio en RAM para cargarlo... 
        2 opciones:
        1. EXPLOTAR LA APLICACION. OutOfMemoryError
        2. Vaciar la cache.

Muchas veces usamos en JAVA la clase HashMap<K,T>.
Esa clase nos permite guardar en memoria un conjunto de datos de tipo T, a los que puedo acceder por una clave de tipo K.

El problema es que la tabla guarda REFERENCIAS (PUNTEROS) a los objetos de tipo T, y si esos objetos son muy grandes, la memoria RAM se puede llenar muy rápido.

Hay una clase llamada WeakHashMap<K,T> que tiene el mismo comportamiento que HashMap<K,T>, pero que permite al recolector de basura de JAVA eliminar los objetos de tipo T cuando no quede memoria RAM disponible. Es el tipo de objeto que necesitamos cuando montamos una cache.

# Principios en el mundo del software

- SoC (Separation of Concerns) Separación de preocupaciones. 

> CUANDO ESTOY CREANDO UN COMPONENTE, ME CENTRO EN ESE COMPONENTE, y me olvido de los demás.

- DRY (Don't Repeat Yourself) No te repitas.

> CUANDO ESTOY CREANDO UN COMPONENTE, SI HAY CODIGO QUE YA EXISTE EN OTRO COMPONENTE, O DENTRO DEL MISMO, NO LO VUELVO A ESCRIBIR, SINO QUE LO REUTILIZO.

Hay además 5 principios muy importantes que se conocen como SOLID.

Puedo respetarlos o no. Es mi decisión. Lo que tengo garantizado es que si los respeto, mi código será más fácil de mantener y evolucionar.
- S - SRP: Single Responsibility Principle. Principio de responsabilidad única.
- O - OCP: Open/Closed Principle. Principio de abierto/cerrado.
- L - LSP: Liskov Substitution Principle. Principio de sustitución de Liskov.
- I - ISP: Interface Segregation Principle. Principio de segregación de interfaces.
- D - DIP: Dependency Inversion Principle. Principio de inversión de dependencias.

    Un componente de alto nivel no debe depender de IMPLEMENTACIONES CONCRETAS de componentes de bajo nivel, sino que ambos deben depender de abstracciones (interfaces).

        Aplicacion   -> InterfazDeUsuario
                            ^
                            |
                      InterfazDeUsuarioConsola

Para resolver esto, la forma guay Y LA QUE SE USA HOY EN DIA es aplicar
lo que se llama un patrón de INYECCION DE DEPENDENCIAS (SPRING)

Antes, usábamos otros patrones. Siguen siendo válidos... pero son más trabajosos que la inyección de dependencias. En concreto, en nuestro ejemplo, vamos a comenzar usando un Patrón Factoria (Factory Pattern).

## CLASSPATH de java

Cuando java (la máquina virtual) ejecuta un programa, necesita saber dónde están los ficheros .class que contienen el código de los componentes que forman parte del programa.

Lo buscan en los directorios que se le indiquen en el CLASSPATH. El CLASSPATH es una variable de entorno del sistema operativo.

Conceptualmente es similar al PATH.
Ayer al instalar JAVA y MAVEN, cambiamos la variable de entorno PATH para que el sistema operativo pueda encontrar los ejecutables de JAVA y MAVEN.

Esta variable no se usa solo para CLASES.
Tambien la podemos usar para otro tipo de recursos, como por ejemplo ficheros de propiedades, ficheros de configuración, o nuestros diccionarios.

---

# El programa está acabado. Ahora toca probarlo y ver si funciona.

PERO ... NO VAMOS A PROBAR NOSOTROS EL PROGRAMA.
Nos llevaría mucho tiempo y además no es fiable, me puedo equivocar.

Además:
- A este programa le haremos haciendo cambios en el futuro.
  Cada vez que hagamos un cambio, tendremos que volver a probar el programa para ver si sigue funcionando correctamente.
  Y esto implicará no hacer solo 1 prueba, sino muchas pruebas, para ver si TODO sigue funcionando correctamente.

Las pruebas manuales... que antes se hacían mucho, llevaban mucho tiempo.

Hoy en día las automatizamos.
Esto es algo OBLIGATORIO HOY EN DIA EN LAS EMPRESAS.

Cualquier empresa que produce software, antes de que el software pase a producción, tiene programas (SONARQUBE) que revisan el código y revisan que al menos haya pruebas automatizadas que revisen en torno al 80-90% de las líneas de código (A ESTO SE LE LLAMA COBERTURA DE PRUEBAS).

Si no se llega a ese porcentaje, el software no pasa a producción.
Y ESE TRABAJO ESTA AUTOMATIZADO, NO LO HACE UN HUMANO.

Es decir, no tengo a quién llorar!

Básicamente se trata de hacer un programa que pruebe nuestro programa. 
De hecho lo que hacemos serán decenas de programas que vayan probando cada parte de nuestro programa.

De nada me vale hacer una prueba que pruebe la aplicación entera
Al menos inicialmente no me vale de nada.
Si la prueba falla, no sabré qué parte/componente de la aplicación ha fallado.

Queremos ir probando cada componente de la aplicación por separado, y luego ir probando la integración de los componentes entre sí.

Para hacer estas pruebas utomatizadas nos apoyamos en Librerías de pruebas unitarias. En JAVAm la más conocida es JUnit.

Es una librería que NO VIENE CON JAVA, sino que hay que descargarla e instalarla. Realmente NOSOTROS NO VAMOS A HACERLO. Ese trabajo se lo vamos a pedir a MAVEN, que una de las cosas que hace es descargar e instalar librerías de terceros, como JUnit (GESTIONAR DEPENDENCIAS).

De JUnit hoy en día usamos la versión 5, que es la más moderna y potente. Pero hay que tener cuidado, porque hay muchos ejemplos en internet de JUnit 4, que es una versión antigua y obsoleta.

Vamos a ir al fichero pom.xml (el fichero de configuración de MAVEN para nuestro proyecto) y vamos a añadir la dependencia de JUnit 5.