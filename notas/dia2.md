
Visual Studio Code (Instalador) -> Entorno de desarrollo / Editor de texto

OpenJDK (JAVA) versión 21. ZIP
MAVEN (programa que ayuda con tareas de nuestro proyecto). ZIP

c:\>Usuarios\NOMBRE\Escritorio\formacionJEE\
                                            proyectos\ <- Esta carpeta la abrimos en VSCODE
                                            java\
                                                lib\
                                                bin\
                                                    javac.exe
                                                    java.exe
                                                ...
                                            maven\
                                                lib\
                                                bin\
                                                    mvn.cmd
                                                ...


Explorador de archivos de Windows

    Equipo \ BOTON DERECHO DEL RATON: PROPIEDADES > VARIABLES DE ENTORNO
    Mi PC  /

    PATH -> Editar -> Nueva -> c:\Usuarios\NOMBRE\Escritorio\formacionJEE\java\bin
                               c:\Usuarios\NOMBRE\Escritorio\formacionJEE\maven\bin
    
    JAVA_HOME -> c:\Usuarios\NOMBRE\Escritorio\formacionJEE\java

---

# Maven

Herramienta importantísima para trabajar con JAVA.
Hay una alternativa a MAVEN que se llama GRADLE, pero en este curso vamos a usar MAVEN.

Nos ayuda a automatizar tareas de nuestro proyecto:
- Compilación
- Empaquetado
- Ejecución
- Ejecución de pruebas automatizadas
- Generar documentación
- Gestión de dependencias!!!! IMPORTANTISIMO

Cuando creamos un programa, escribimos mucho código... pero también usamos muchas librerías de terceros. Esas librerías es lo que llamamos DEPENDENCIAS.

Antiguamente a los desarrolaldores nos tocaba buscar esas librerías en internet, descargarlas y copiarlas a carpetas en nuestro proyecto... ESO ERA MUY TEDIOSO.
El problema no era solo buscar una dependencia. El problema es que esas dependencias van cambiando de versión... VAN SALIENDO VERSIONES NUEVAS DE ESAS DEPENDENCIAS/LIBRERIAS con nuevas funcionalidades, con arreglos, y necesitamos tenerlas actualizadas -> NOS PASABAMOS EL DIA ENTERO BUSCANDO LIBRERIAS, DESCARGANDO, DESCARGANDO OTRA VEZ LAS ACTUALIZACIONES... COPIANDOLAS A CARPETAS... ESO LO HACE POR NOSOTROS MAVEN.
Además, yo puedo necesitar una librería.. pero esa librería puede necesitar a su vez otras 10... que necesiten otras 10... y así sucesivamente. Eso es lo que llamamos DEPENDENCIAS TRANSITIVAS. Maven se encarga de buscar todas esas dependencias transitivas y descargarlas por nosotros.

Muy habitual en un proyecto de tamaño medio es acabar con más de 100 dependencias. PONERME A BUSCAR YO POR INTERNET TODAS ESAS LIBRERIAS ME LLEVARIAS DIAS! MAVEN RESUELVE TODO ESE TRABAJO.

Cuando creamos un proyecto y lo gestionamos mediante MAVEN, maven impone un estructura de carpetas para trabajar.

    MI-PROYECTO\
        src\            <- Aquí tendremos el código de nuestro proyecto
           main\        <- Aquí tendremos el código de nuestro programa/aplicación
             java\      <- Aquí tendremos el código JAVA de nuestro programa/aplicación
             resources\ <- Aquí tendremos otros archivos necesarios para nuestro programa
           test\        <- Aquí tendremos el código de pruebas automatizadas de nuestro 
                           programa/aplicación
             java\      <- Aquí tendremos el código JAVA de nuestras pruebas automatizadas
             resources\ <- Aquí tendremos otros archivos necesarios para nuestras pruebas automatizadas
        target\         <- Cada vez que maven haga algo con nuestro proyecto, 
                            se trabaja en esta carpeta. La crea MAVEN AUTOMATICAMENTE
            classes\
            test-classes\
        pom.xml <- Este archivo es el archivo de configuración de maven para mi proyecto
                   Dentro pondremos muchas cosas.

# Que tareas le puedo pedir a maven que haga por mi?

Maven ejecuta tareas... Realmente cada tarea es ejecutada por un plugin.
Yo le pido a maven que ejecute una tarea y maven busca el plugin que se encarga de ejecutar esa tarea y le pide a ese plugin que ejecute la tarea. 

Por ejemplo, si le pido a maven que compile mi proyecto, maven busca el plugin que se encarga de compilar y le pide a ese plugin que compile mi proyecto.

ESO ES UNA FORMA DE TRABAJAR CON MAVEN... PERO NO ES LA FORMA HABITUAL

Maven define en paralelo lo que se llama el ciclo de vida de un proyecto. DEFINE FASES (GOALS)
Esos goals son secuenciales:

    resources       Copiar lo que tengo en la carpeta src/main/resources 
                        a la carpeta target/classes
        V
    compile         Compilar los archivos .java que tengo en la carpeta src/main/java 
                        y poner los .class resultantes en la carpeta target/classes
        V
    test            Copiar lo que tengo en la carpeta src/test/resources 
                        a la carpeta target/test-classes    
                    Y además, compila los archivos .java que tengo en la carpeta src/test/java 
                        y poner los .class resultantes en la carpeta target/test-classes
        V
    package         Empaquetar en un archivo ZIP (aunque le pone extensión .jar, .war, o .ear)
                       todo lo que tengo en la carpeta target/classes y ponerlo en la carpeta target
        V
    install         YA OS LO CONTARE

    ---

    clean           Borrar la carpeta target

