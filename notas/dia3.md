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