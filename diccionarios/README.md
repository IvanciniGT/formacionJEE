
# Aplicación diccionarios

Queremos un programa que podamos ejecutar desde una terminal de comandos:

    # Buscar una palabra que existe en el diccionario solicitado
    c:\> buscarPalabra "melón" 
    Aplicación de Diccionarios v1.1.0
    No ha suministrado los parámetros necesarios. 
    Debe suministrar la palabra a buscar y el idioma del diccionario.
    Ejemplo:
        c:\> buscarPalabra "melón" "es"
    Gracias por usar nuestra aplicación de diccionarios.

    # Buscar una palabra que existe en el diccionario solicitado
    c:\> buscarPalabra "melón" "es"
    Aplicación de Diccionarios v1.1.0
    La palabra melón existe en el diccionario de español, y significa:
    - Fruto del melonero
    - Persona con pocas luces: "Eres un melón!"
    Gracias por usar nuestra aplicación de diccionarios.

    # Buscar una palabra que no existe en el diccionario solicitado
    c:\> buscarPalabra "melón" "en"
    Aplicación de Diccionarios v1.1.0
    La palabra no melón existe en el diccionario de inglés.
    Gracias por usar nuestra aplicación de diccionarios.

    # Buscar una palabra en un diccionario desconocido
    c:\> buscarPalabra "melón" "elfos"
    Aplicación de Diccionarios v1.1.0
    Lo siento, pero no tengo diccionario élfico.
    Gracias por usar nuestra aplicación de diccionarios.

# Nuestra aplicación tendrá distintos componentes.

No queremos una aplicación que sea 1 fichero... acabaría siendo enorme y difícil de mantener. Queremos una aplicación que tenga distintos componentes, CADA COMPONENTE DEBE TENER UNA RESPONSABILIDAD.

Al llevar estas ideas a CODIGO JAVA, cada COMPONENTE SERÁ UNA CLASE. Y cada clase irá en un archivo de texto .java.

La idea es que mi aplicación está compuesta por distintos componentes / clases.
Además, esos componentes deben ser reemplazables fácilmente. SI CONSIGO ESTO, MI SISTEMA SERÁ FACILMENTE MANTENIBLE <- ESTE ES EL OBJETIVO PRINCIPAL AL CREAR UN PROGRAMA DE SOFTWARE.

El que un programa funcione, eso se da por descontado... EVIDENTEMENTE. Si el programa no funciona, NO ES UN PROGRAMA, es una colección de ficheros de texto con pretensiones. 


Que un coche ande se da por descontado. LA CLAVE DE UN COCHE NO ES QUE ANDE (si no anda, no es un coche... es una escultura de hierro con ruedas de goma).
Lo que marca la diferencia entre un buen coche y un coche que no es bueno es que sea FACIL DE MANTENER y que dé pocos problemas.

Un coche tiene muchos componentes:
- RUEDAS
- ALTERNADOR
- BUJIAS
- CORREA DE DISTRIBUCION
- BATERIA
- AIRBAGS
- ... Cientos de componentes

Y esos componentes se comunican entre si... guardan relaciones entre ellos.
Ahora, cada componente TIENE SU MISION, SU RESPONSABILIDAD. Cada componente hace una cosa, y la hace bien. No quiero componentes que hagan varias cosas:
- Si se jode el componente me quedo sin muchas funcionalidades del coche.
- Si se jode un componente que hace 1 SOLA COSA, si el componente se rompe/estropea, me quedo sin esa funcionalidad, pero el resto del coche sigue funcionando.

Además, querré que cada componente sea fácilmente reemplazable.
- RUEDAS: Las ruedas deben ser reemplazables (desgaste, funcionalidad - ruedas de verano y de invierno)
- ALTERNADOR: Por rotura lo querré cambiar... Pero quizás lo quiero cambiar porque instalo un equipo de música más potente y el alternador que tengo no da suficiente potencia. Quiero poder cambiarlo fácilmente.
- BATERIA

Para que los componentes sean fácilmente reemplazables usamos/definimos ESTANDARES!
- RUEDA: 
    - Tamaño: Diámetro, Ancho, Perfil
    - Tipo de neumático: Verano, Invierno, All Season
    - Código de velocidad: H, V, W, Y

    Los fabricantes harán ruedas cumpliendo con esas especificaciones, estándares...
    Y eso me permite fácilmente reemplazar una rueda por otra de otro fabricante, siempre que cumpla con el estándar.

    Mi coche tiene ruedas: 17 pulgadas, 225 de ancho, perfil 45, código de velocidad V.
        Y ahora puedo poner cualquier rueda que cumpla con esas especificaciones, aunque sea de otro fabricante.
    
    La especificación es una rueda? NO... La especificación es algo ABSTRACTO, INTANGIBLE (no puedo tocarlo), son códigos, normas, cosas con las que la rueda debe cumplir.
    Por su lado, una ruda concreta de un fabricante es algo CONCRETO, TANGIBLE (puedo tocarlo), es un objeto físico que cumple con la especificación.

    Esto lo llevamos a código JAVA.

        - Una especificación en JAVA es lo que llamamos una INTERFAZ.
          - Rueda para MI COCHE: 17 pulgadas, 225 de ancho, perfil 45, código de velocidad V.
        - Un modelo concreto de rueda cumpliendo con una especificación es lo que llamamos una CLASE. PERO OJO... La clase TAMPOCO ES UNA RUEDA... Es un MODELO DE RUEDA, un objeto que cumple con la especificación de rueda.
          - PIRELLI XB17J: 17 pulgadas, 225 de ancho, perfil 45, código de velocidad V.
          Pero el modelo, no es la rueda.
        - Lo que tendré luego son miles de ruedas de ese modelo. Eso es lo que java llamamos una INSTANCIA de una clase. ESTO ES LO CONCRETO, TANGIBLE, PUEDO TOCARLO. Es un objeto físico que cumple con la especificación de rueda. 
           
# Hoy en día

El trabajo hoy en día, NO ES ESCRIBIR CODIGO (esto lo escriben los agentes de las IAs).
Mi trabajo es entender / diseñar qué componentes necesito para mi sistema, definir sus especificaciones. Después, le pediré a una IA que me escriba el código de cada componente, y luego yo lo probaré y lo integraré en mi sistema.


# Componentes que necesitamos para nuestra aplicación de diccionarios

| Componente        | Responsabilidad                                                            |
|-------------------|----------------------------------------------------------------------------|
| Diccionario       | Saber si una palabra existe en el diccionario y devolver los significados. |
| SuministradorDeDiccionarios | Saber qué diccionarios existen y suministrarlos.                 |
| InterfazDeUsuario | Interactuar con el usuario, mostrar resultados y pedir datos.              |
| Aplicacion        | Coordinar los componentes y ejecutar la aplicación.                        |

Luego... podré tener diccionarios ONLINE, en papel, en un fichero de texto, en una base de datos... a mi primo, que es muy listo y se sabe todas las palabras... Y LE USO COMO SI FUERA UN DICCIONARIO.


Diccionario es una INTERFAZ... una ESPECIFICACION... DEFINE LO QUE DEBE PODER HACERSE CON UN DICCIONARIO, es como la especificación de mi rueda. NO ES UN DICCIONARIO. Ni la especificación de una rueda es una rueda. 

Puedo tener ahora MODELOS de RUEDA, o MODELOS DE DICCIONARIO (CLASES) que cumplan con la especificación de diccionario. Por ejemplo:
- DiccionarioEnBBDD
- DiccionarioEnFicheroTXT
- DiccionarioEnFicheroJSON
- ...

Pero eso son CLASES, MODELOS (Como el modelo de rueda PIRELLI XB17J). No son diccionarios, ni ruedas...  Son modelos de diccionario que cumplen con la especificación de diccionario.

Luego habrá RUEDAS concretas que MONTO EN EL COCHE, de ese modelo, cumpliendo con esa especificación de rueda. Y habrá DICCIONARIOS concretos que USO EN MI APLICACION, de uno de esos modelos, cumpliendo con esa especificación de diccionario.

El SuministradorDeDiccionarios es una INTERFAZ... una ESPECIFICACION... DEFINE LO QUE DEBE PODER HACERSE CON UN SUMINISTRADOR DE DICCIONARIOS, pero no es un suministrador de diccionarios.

Habrá suministradores de diccionarios online, o suministradores de diccionarios en papel = MODELOS

Suministradores concretos: 
- AMAZON (suministrador de diccionarios online)
- BIBLIOTECA LOCAL (suministrador de diccionarios en papel)
- LIBRERO DE LA ESQUINA (suministrador de diccionarios en papel)


# Aplicacion

¿Qué debe hacer la aplicación? ¿Cuál es el algoritmo de mi programa?
¿ Cuales son las tareas que se ejecutan en mi programa? ¿Qué hace mi programa?

El programa ARRANCA y entonces?

- Le pide al componente InterfazDeUsuario que muestre el mensaje de bienvenida.

- Le preguntamos al componente InterfazDeUsuario qué palabra quiere buscar el usuario.

- Si el usuario no ha suministrador palabra: 
  - Le pedimos al componente InterfazDeUsuario que muestre un mensaje de ayuda
- Si ha puesto palabra:
  - Le preguntamos al componente InterfazDeUsuario qué idioma quiere usar el usuario.
  - Si el usuario no ha suministrador idioma: 
    - Le pedimos al componente InterfazDeUsuario que muestre un mensaje de ayuda
  - Si ha puesto idioma:
    - Le pregunto al componente SuministradorDeDiccionarios si tiene diccionario de ese idioma.
    - Si no tiene diccionario de ese idioma:
      - Le pide al componente InterfazDeUsuario que avise de idioma desconocido.
    - Si tiene diccionario de ese idioma:
      - Le pedimos al componente SuministradorDeDiccionarios que nos suministre el diccionario de ese idioma.
      - Le pedimos al componente Diccionario que nos diga si la palabra existe en el diccionario.
      - Si la palabra no existe en el diccionario:
        - Le pide al componente InterfazDeUsuario que avise de palabra desconocida.
      - Si la palabra existe en el diccionario:
        - Le pide al componente Diccionario que nos suministre los significados de la palabra.
        - Le pide al componente InterfazDeUsuario que muestre los significados de la palabra
  
- Le pide al componente InterfazDeUsuario que muestre el mensaje de despedida.

---

# Diseño del sistema

    Aplicacion 
        |
        +----> InterfazDeUsuario            <----- InterfazDeUsuarioConsola
        |                                   <----- InterfazDeUsuarioDeEscritorio
        |
        +----> SuministradorDeDiccionarios  <----- SuministradorDeDiccionariosEnFicheros
        |           |                       <----- SuministradorDeDiccionariosEnBBDD
        |           V
        +----> Diccionario                  <----- DiccionarioEnFichero
                                            <----- DiccionarioEnBBDD
                ^^^^^^^                                 ^^^^^
              Interfaces                          Implementaciones
            
            - Al ser interfaces, podremos reemplazar las implementaciones de cada componente fácilmente, sin afectar al resto del sistema.

# Ejecución de la aplicación:

c:\> mvn package
c:\> java -jar target/diccionarios-1.1.0.jar "melón" "es"

