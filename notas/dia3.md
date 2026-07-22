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