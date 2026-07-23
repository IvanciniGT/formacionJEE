
# Nuestra app de diccionarios sencilla

La aplicación ya le tenemos operativa.
El problema es que dijimos que no basta con que una aplicación funcione.
La clave es que la aplciación envejezca bien, que sea mantenible, que sea fácil de entender y de modificar, de evolucionar.

Y NUESTRA APLICACION AHORA MISMO NO LO ES! NI CERCA!

Tiene varios problemas:
1. Tenemos 10 archivos en una carpeta tirados, sin ningún orden ni estructura.
   10 archivos no parecen muchos... y no lo son. Pero esto es una aplicación de ejemplo y muy sencilla.
   En un sistema real, acabamos con cientos de archivos, y si no tenemos una estructura clara, es un caos.
   Cuando alguien entre a modificar algo... va a pasar mucho tiempo tratando de investigar e identificar qué hace cada cosa y cómo se relaciona con el resto de la aplicación.
2. La arquitectura de la aplicación es muy básica. MUY SIMPLE.
   Y esto va a generar problemas de mantenimiento y evolución enormes...
   Los costes de mantenimiento y evolución de una aplicación se van a disparar en cuanto comience a usarse.

# Problemas con la arquitectura de nuestra aplicación

Vamos a imaginar cambios posibles en la aplicación y cómo nos afectaría a nosotros (desarrolladores), a los usuarios de la aplicación y a la gente de operaciones (que la tiene que instalar y mantener en producción).

- Cambio en la UI de la aplicación
- Añadir una funcionalidad a la aplicación (por ejemplo, que si una palabra no se encuentra, que ofrezca las palabras similares que sí existen en el diccionario).
        "melon" -> "melon" no existe, pero sí existen "melón"
- Añadir un diccionario nuevo en un nuevo idioma.
- Añadir palabras en diccionario existente o modificar significados de palabras existentes.
  - Para los desarrolladores: 
    Realmente no es un trabajo que deban hacer los desarrolladores.
    Lo hará un lingüista experto en el idioma, que no sabe programar.
    Ese creará un archivo de texto con las palabras y significados, y nos lo dará a nosotros:
        - Por email (RUINA!)
        - Que lo suba a un repositorio de control de versiones (GIT, SVN, ...) GUAY!
          El diccionario debe ir versionado.
          En este caso, los diccionarios están junto con el código de la aplicación, y eso no es lo ideal:
            - Por error, un lingüista podría modificar el código de la app op borrar un archivo de código y romper la aplicación.
              Además, la estructura de carpetas que verá el lingüista es compleja:
                 src/main/java/... y src/test/java/... y src/main/resources/... y src/test/resources/...
                 Y eso es un lío para alguien que no sabe programar. DONDE TOCO?
    Si bien, los desarrolladores no tocan ni crean ni modifican esos archivos, son los que deben empaquetar la aplicación (generar el .jar) que se distribuye.
  - Para los usuarios de la aplicación:
    - Les toca REINSTALAR LA APLICACION cada vez que se añade o modifica un diccionario.
      Nuestra app se ejecuta localmente en el ordenador del usuario, y no hay un servidor central con los diccionarios. Cada usuario tiene su propia instalación de la aplicación y de los diccionarios.
      Además, no tengo control (como empresa) de qué versión de la aplicación/diccionarios tiene cada usuario. Cada uno puede tener instalada una versión distinta. Y cada uno estar recibiendo respuestas distintas a la misma palabra.

      La reinstalación además es algo que se complica a un usuario básico. El usuario sabe de su negocio... no de instalar aplicaciones.
  - La gente de operaciones... En este caso entran poco.
    Se encargarán de publicar la nueva versión de la aplicación y de los diccionarios, en algún sitio donde los usuarios puedan descargarla. También de notificarles que hay una nueva versión disponible. PERO NO TIENEN VISIBILIDAD DE QUÉ VERSION TIENE CADA USUARIO. 
    Cuando haya un problema, y un usuario mandé un ticket al CAU (Centro de Atención a Usuarios), el CAU no sabrá qué versión de la aplicación/diccionarios tiene el usuario, y eso dificultará mucho la resolución del problema.

Mi trabajo es adelantarme a estas situaciones y problemas, y diseñar la aplicación de forma que sea fácil de mantener y evolucionar.

# Propuesta 1

Esta no va a ser la propuesta/solución definitiva... pero es un primer paso para mejorar la arquitectura de la aplicación.

Ahora mismo, sólo tenemos una aplicación CLIENTE, que se ejecuta en el ordenador del usuario y que tiene los diccionarios en su interior.

Podemos pasar a una arquitectura cliente-servidor, donde:
- mantengamos una aplicación cliente, pero que solo sea interfaz gráfica
- Montamos una aplicación en un servidor central que tiene los diccionarios y les da respuestas a la aplicación cliente.


    Usuario -> java -jar app.jar "melón" "es"
                                             --- ????       --> Servidor   (es el que tiene los diccionarios)
                                             <-- respuesta --- "melón" existe, significados: fruta, color, ...>

Si montamos esto, ya empezamos a eliminar algunos de los problemas que habíamos identificado.

- Si hay un diccionario nuevo
- Si hay un diccionario modificado
  Ese cambio se instala en el servidor central (por parte de un experto-operaciones-), y todos los usuarios de la aplicación cliente se benefician de ese cambio sin tener que reinstalar la aplicación. 
  Además, me aseguro que todos los usuarios tienen la misma versión de los diccionarios, y que todos reciben la misma respuesta a la misma palabra.
  Cuando alguien tenga un problema y mande un ticket al CAU, el CAU sabrá qué versión de la aplicación/diccionarios tiene el usuario, y eso facilitará mucho la resolución del problema.

Hay otros problemas que aún no resolvemos:
- Si quiero un cambio en la UI de la aplicación cliente, tengo que hacer que todos los usuarios se descarguen una nueva versión de la aplicación cliente.
- Si añado funcionalidad que afecte a la UI de la aplicación cliente, tengo que hacer que todos los usuarios se descarguen una nueva versión de la aplicación cliente.
- Pero cambios que no afecten a la UI de la aplicación cliente, como por ejemplo: que si en lugar de escribir la palabra "melón", escribo "MELÓN" (con mayúsculas), que la aplicación me devuelva la misma respuesta, eso sí lo puedo hacer en el servidor central y todos los usuarios se benefician de ese cambio sin tener que reinstalar la aplicación cliente.

# Cómo puedo hacer que mi app cliente se comunique con mi app servidor?

Lo primero que debo es de entender cómo es la comunicación entre el cliente y el servidor.
- Es unidireccional o bidireccional?
  Es siempre el cliente el que inicia la comunicación, o puede ser el servidor el que inicie la comunicación? En un caso como este, y en el 90% de los casos, es el cliente el que inicia la comunicación. El servidor está a la escucha de peticiones de los clientes, e ir respondiendo a esas peticiones.

  Hay pocos (pero algunos) casos donde el servidor inicia la comunicación con el cliente. Por ejemplo, cuando un cliente se conecta a un servidor de chat, y el servidor le envía mensajes de otros usuarios conectados al chat.

Dependiendo de esto, necesitaré un mecanismo de comunicación u otro.
En la mayor parte de los casos, vamos a trabajar con comunicación unidireccional, donde el cliente inicia la comunicación y el servidor responde a esa comunicación.

Lo siguiente que necesito definir es el protocolo de comunicación entre el cliente y el servidor.
Protocolo son reglas que definen cómo se comunican entre sí dos sistemas.

A lo largo de la historia, han surgido muchos protocolos de comunicación entre sistemas. Algunos de ellos son:
- CORBA (Common Object Request Broker Architecture)
- RMI (Remote Method Invocation)
Estos 2 ya están totalmente obsoletos y no se usan ya en la industria.
- Lo que permitían ambos era (en JAVA) que dentro de mi código pudiera usar OBJECTOS (instancias de clases) que realmente se estaban ejecutando en otro ordenador (en el servidor). Para mí, como programador, era como si esos objetos estuvieran en mi propio ordenador.

Esto tuvo su momento... pero pasó.
- CORBA directamente fué eliminado de la JVM JAVA en la versión 11. 
- RMI sigue existiendo en la JVM, pero ya no se usa en la industria.

Más adelante salió un protocolo llamado SOAP (Simple Object Access Protocol). 
Este protocolo se basa en archivos XML que el cliente envía al servidor, y el servidor respondía (con otro archivo XML) al cliente.
Esos archivos a su vez se mandaban dentro de mensajes HTTP (el mismo protocolo que usan los navegadores web para comunicarse con los servidores web).

Esto se usó bastantes años... pero de nuevo quedó obsoleto y ya no se usa en la industria.
ERA MUY COMPLEJO de programar, y además los archivos XML eran muy grandes y pesados, y eso hacía que la comunicación fuera lenta.

Ejemplo en nuestro caso de diccionarios usando SOAP:
```xml
<!-- Mensaje que envía el cliente al servidor -->
<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:dic="http://www.diccionarios.com">
   <soap:Header/>
   <soap:Body>
      <dic:buscarPalabra>
         <dic:palabra>melón</dic:palabra>
         <dic:idioma>es</dic:idioma>
      </dic:buscarPalabra>
   </soap:Body>
</soap:Envelope>
```

Para 7 caracteres estoy mandando más de 300 caracteres... y eso es un SIN SENTIDO!

El servidor respondería con otro archivo XML, que también sería muy grande y pesado.

```xml
<!-- Mensaje que envía el servidor al cliente -->
<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:dic="http://www.diccionarios.com">
   <soap:Header/>
   <soap:Body>
      <dic:buscarPalabraResponse>
         <dic:existe>true</dic:existe>
         <dic:significados>
            <dic:significado>fruta</dic:significado>
            <dic:significado>color</dic:significado>
         </dic:significados>
      </dic:buscarPalabraResponse>
   </soap:Body>
</soap:Envelope>
```

Esto dejó de usarse hace ya más de 10 años, y hoy en día ya no se usa en la industria.
Puede quedar algún sistema "legacy" que aún use SOAP, pero ya no se desarrollan nuevos sistemas con SOAP.

Lo que se ha impuesto en la industria y usa TODO EL MUNDO SIN EXCEPCION desde hace años.. y tiene pinta de que va a seguir siendo así durante muchos años... es un protocolo llamado REST (Representational State Transfer).

Realmente REST no es un protocolo, sino un conjunto de restricciones sobre el protocolo HTTP (el mismo que usan los navegadores web para comunicarse con los servidores web) para comunicar sistemas entre sí.

Nuestra aplicación cliente lo que hace es mandar una petición HTTP al servidor, y el servidor le responde con otra respuesta HTTP. Caso que sea necesario (solo si es necesario) se mandarán entre si mensajes en formato JSON (JavaScript Object Notation), que es un formato de datos muy ligero.

```json

{
  "palabra": "melón",
  "idioma": "es"
}
```

```json
{
  "existe": true,
  "significados": ["fruta", "color"]
}
```

Aunque lo cierto es que en muchos casos, no mandamos ni eso!
Muchas veces, cuando es necesario mandar poca información, la mandamos como parte de la URL de la petición HTTP.

Por ejemplo, nuestra aplicación cliente de diccionarios, podría mandar una petición HTTP al servidor con la siguiente URL:

    > http://diccionarios.miempresa.com/es/melón

Y el servidor entonces si contestaría con un JSON del tipo:

```json
{
  "existe": true,
  "significados": [
    "Fruta comestible de la planta Cucumis melo, de pulpa jugosa y dulce, con semillas en su interior y cubierta por una piel dura y rugosa.",
    "Persona con pocas luces, torpe o lenta para comprender las cosas."
  ]
}
```

Es importante entender cómo funciona el protocolo HTTP. Y es bastante sencillo.

HTTP es un protocolo síncrono unidireccional. Es decir:
> "UNIDIRECCIONAL": Siempre es el cliente el que inicia la comunicación,mandando una petición HTTP al servidor (HTTP REQUEST), y el servidor responde con una respuesta HTTP al cliente (HTTP RESPONSE).
> "SINCRONO": El cliente queda a la espera de la respuesta del servidor, y no puede hacer nada más hasta que reciba esa respuesta.

    Cliente -> HTTP REQUEST -> Servidor -> HTTP RESPONSE -> Cliente

Otra cosa. Cuando mandamos algo por HTTP, tenemos siempre 3 partes:
    - URL (dirección del servidor a la que hago la petición)
    - HEADERS (ETIQUETAS)
    - MENSAJE (BODY)

Imaginad una petición como un envío que hago por correos. Le quiero mandar algo a mi primo.
- Lo que sea que mando, lo meto en una CAJA (BODY)
- Fuera de la caja, pego una pegatina con información adicional (METADATOS) (HEADERS)
   - DELICADO
   - PESO: 1kg
   - URGENTE
   - Remitente: YO
   - MOTIVO: REGALO, DEVOLUCION, ERROR


Realmente en HTTP siempre mandamos una CAJA con pegatina (HEADERS) aunque puede esta vacía (SIN BODY)

    Cliente -> HTTP REQUEST (URL + HEADERS + BODY-opcional-) 
                    -> Servidor -> HTTP RESPONSE (HEADERS + BODY-optional-) -> Cliente

HAy 2 ETIQUETAS (HEADERS) muy importantes que debemos conocer (con el tiempo conoceremos más):
- VERBO/METODO HTTP: (este dato lo manda el cliente, y es obligatorio)
  - GET: El cliente quiere datos del servidor.
  - POST: El cliente quiere enviar datos al servidor.
  - PUT: El cliente quiere modificar datos en el servidor.
  - DELETE: El cliente quiere borrar datos en el servidor.
  - ...
    Los navegadores, por defecto, cuando los uso para consultar sitios web, siempre usan el verbo GET. 
- STATUS CODE: (este dato lo manda el servidor, y es obligatorio)
  Es un número de 3 cifras.
   - 2??: Si la cosa ha ido bien.
       - 200: OK. Todo ha ido bien.
       - 201: Created. Se ha creado un recurso en el servidor.
   - 3??: Si el cliente debe reenviar la petición a otro sitio (REDIRECCION)
   - 4??: Si el cliente ha hecho(mandado) algo mal (ERROR DEL CLIENTE)
     - 400: Petición incorrecta. El cliente ha mandado algo que el servidor no entiende.
     - 401: No autorizado. El cliente no tiene permisos para acceder a ese recurso.
     - 403: Prohibido. El servidor entiende la petición pero se niega a autorizarla.
     - 404: No encontrado. El recurso solicitado no existe en el servidor.
   - 5??: Si el servidor ha hecho algo mal (ERROR DEL SERVIDOR)

En nuestro caso: 
    
    Cliente: 
        GET+http://diccionarios.miempresa.com/<idioma>/<palabra>

    Servidor:
        status: 404 Not Found (el diccionario o la palabra no existen)
            {"diccionario": "true", "palabra": "false"}

        status: 200 OK (el diccionario y la palabra existen)
            {"diccionario": "es", "palabra": "melón", "significados": ["fruta...", "color..."]}
        
        status: 500 Internal Server Error 
            {"error": "La BBDD donde están los diccionarios no está disponible"}

Esto es lo primero que vamos a montar.
Como decía, lo primero será cambiar nuestra aplicación a una arquitectura CLIENTE - SERVIDOR.

Tendremos una parte de la app que se ejecuta en el ordenador del usuario (CLIENTE), desde linea de comandos, igual que antes.

Y tendremos otra parte que se ejecuta en un servidor central (SERVIDOR).

---

Más adelante, montaremos una aplicación WEB de cliente, que se comunicará con nuestra aplicación SERVIDOR, y que permitirá a los usuarios usar la aplicación desde un navegador web, sin necesidad de instalar nada en su ordenador.

Esto lo haremos la semana que viene el viernes ( en esa clase que vamos a dar por la mañana sobre IAs )


    Estado actual:          App UNICA (frontal/interfaz gráfica consola + lógica de diccionarios + diccionarios)

    Primer paso:            App CLIENTE (frontal/interfaz gráfica consola) 
                                    v
                                  REST
                                    v
                            App SERVIDOR (lógica de diccionarios + diccionarios)

    Último paso:            App WEB CLIENTE (frontal/interfaz gráfica web) 
                                    v
                                  REST
                                    v
                            App SERVIDOR (lógica de diccionarios + diccionarios)

# Cómo vamos a proceder?

1. Reorganizar "un poco" la estructura de nuestro proyecto.
   Sin tocar funcionalidad: REFACTORIZAR.
2. Vamos a montar la aplicación SERVIDOR... trabajando con diccionarios en FICHEROS igual que ahora mismo
3. Crearemos una nueva versión de la aplicación CLIENTE que se comunique con la aplicación SERVIDOR.
4. Crearemos una nueva versión de la aplicación SERVIDOR que trabaje con diccionarios en BASE DE DATOS.
5. Crearemos una aplicación WEB CLIENTE que se comunique con la aplicación SERVIDOR.

Para montar la aplicación SERVIDOR, vamos a usar un framework de JAVA llamado SPRING BOOT (ya os contaré de esto...)

# Refactorización de nuestro proyecto.

Qué problemas tiene?
- Tenemos 10 archivos tirados en una carpeta, sin ningún orden ni estructura.
- Si quiero cambiar algo, tengo que abrir un proyecto que tiene TODO = FATAL TIO!!!!

Vamos a ir a otro tipo de solución:
No vamos a tener 1 proyecto!
Vamos a acabar con MUCHOS proyectos, cada uno con sus propias carpetas y estructura.

Pero todos integrados dentro de un mismo proyecto global, que llamaremos "diccionarios".

PROYECTO PRINCIPAL y dentro tendremos SUBPROYECTOS (MÓDULOS) para cada componente de la aplicación.

Y cómo se hace esto?
Habíamos creado, 1 proyecto con Maven.
- Nuestro proyecto tenía 1 carpeta src y un archivo pom.xml

Ahora vamos a tener para cada MODULO (SUBPROYECTO) su propia carpeta src y su propio archivo pom.xml.

La carpeta diccionarios que tenemos, la voy a dejar TAL CUAL!
Todo lo nuevo que hagamos, lo vamos a hacer dentro una nueva carpeta llamada diccionarios_v2.


    diccionarios_v2
    ├── diccionarios-api (Interfaces que tengan que ver con la gestión de diccionarios)
    |       |- src
    |       |- pom.xml
    ├── diccionarios-en-ficheros (Implementación de la gestión de diccionarios en ficheros)
    |       |- src
    |       |- pom.xml
    ├── diccionarios-en-bbdd (Implementación de la gestión de diccionarios en base de datos, POR AHORA NO)
    |       |- src
    |       |- pom.xml
    ├── aplicacion-ui-api (Interfaces que tengan que ver con la UI de la aplicación)
    |       |- src
    |       |- pom.xml
    ├── aplicacion-ui-web (Implementación de la UI de la aplicación en web)
    |       |- src
    |       |- pom.xml
    ├── aplicacion-servidor (Implementación de la aplicación servidor)
    |       |- src
    |       |- pom.xml
    ├── aplicacion-cliente-consola (Implementación de la aplicación cliente)
    |       |- src
    |       |- pom.xml
    ├── aplicacion-ui-consola (Implementación de la UI de la aplicación en consola)
    |       |- src
    |       |- pom.xml
    ├── aplicacion-cliente-desktop (Implementación de la aplicación cliente en escritorio java)
    |       |- src
    |       |- pom.xml
    ├── aplicacion-ui-desktop (Implementación de la UI de la aplicación en escritorio java)
    |       |- src
    |       |- pom.xml
    ├── diccionarios
    |       ├── es
    |       |   |- src
    |       |       |- main
    |       |             |- resources - es.txt
    |       |   |- pom.xml
    |       ├── en
    |           |- src
    |           |- pom.xml
    |-- pom.xml (archivo de configuración del proyecto global diccionarios_v2)

ESTE CONCEPTO QUE VAMOS A IMPLEMENTAR, es lo que llamamos una arquitectura de COMPONENTES DESACOPLADOS, basada en CAPAS (LAYERS).