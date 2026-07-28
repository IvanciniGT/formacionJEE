
# Proyecto diccionarios

## Primera versión

    Aplicación cliente:
    - Interfaz de usuario <- Para su uso en una terminal
    - Gestión de diccionarios y búsqueda de palabra <- Diccionarios en ficheros de texto
    - 2 diccionarios: Español e Inglés

    Problemas:
    - Si cambiaban las palabras de uno de los ficheros de diccioanrio (cosa que ocurrirá con seguridad en el futuro)
    - Si añadíamos más diccionarios (cosa que ocurrirá con seguridad en el futuro)
    - Si cambiamos algo de la funcionalidad relativa a cómo se hacen las búsquedas (cosa que ocurrirá con seguridad en el futuro)

    En cualquiera de esos escenarios es necesario redistribuirr la aplicación cliente, lo que es mucho trabajo.
    Por otro lado no tenemos garantías de que todos los clientes actualicen la aplicación cliente, por lo que no podemos garantizar que todos los clientes tengan la misma versión de la aplicación cliente:
        - 2 clientes pueden estar obteniendo resultados distintos para la misma búsqueda, lo que es un problema.
        - En caso de haber un problema tenemos que averiguar qué versión de la aplicación cliente tiene cada cliente, lo que es un sobre-esfuerzo.

    Para resolverlo planteamos una nueva versión de la aplciación:

## Segunda versión

    En este caso creamos / planteamos una verisón de la aplciación que tiene una arquitectura cliente-servidor: 2 aplicaciones:

    Aplicación en el servidor (servicio web):
    - Gestión de diccionarios y búsqueda de palabra <- Diccionarios en ficheros de texto
    - 2 diccionarios: Español e Inglés
    - Crear un módulo/subproyecto que expone la funcionalidad de los diccionarios y la búsqueda de palabras como un servicio web (API REST)

    Aplicación cliente:
    - Interfaz de usuario <- Para su uso en una terminal
    - Gestión de diccionarios y búsqueda de palabra <- Nuevo componente/subproyecto para trabajar con el servicio web

Versión primera:
> Aplicación cliente -> diccionarios-api <- diccionarios-en-ficheros
>                    -> interfaz-de-usuarios <- terminal

Segunda versión:
> Aplicación cliente -> diccionarios-api <- diccionarios-en-servicio-web <-protoc. http-> servicio-web -> diccionarios-api <- diccionarios-en-ficheros
                                            -----------------------------------------------------------
>                    -> interfaz-de-usuarios <- terminal

El cambio fue sencillo: Tan solo añadir los componentes:
- diccionarios-en-servicio-web
- servicio-web (Controlador Rest)

En este caso, la aplciación del servidor la hemos creado con Spring Boot, que nos permite crear un servicio web de manera sencilla y rápida.

Spring/Springboot se ha encargado de muchas cosas:
- Crear un servidor web/apps embebido (Tomcat)
- Instalar nuestra aplciación en el servidor web/apps embebido
- Arrancar el servidor web/apps embebido y nuestra aplicación en él
- Configurar en el servidor web/apps embebido las rutas http a las que nuestra aplicación responde (controladores Rest)
- Se encarga de transformar los objetos JAVA que tenemos en el código a JSON para ser enviados a través de la red y viceversa (transformar JSON que llega a través de la red en objetos JAVA)

Hasta aquí es lo que tenemos implementado.
Vamos a hacer algunos cambios adicionales.

# Tercera versión de nuestro sistema

Vamos a reemplazar los diccionarios en archivos. Eso está un poco cutre.
Vamos a llevar los diccionarios y las palabras con sus significados a una BBDD.
El cambio va a ser muy sencillo! Gracias al buen diseño que tenemos de nuestro sistema.
Y gracias por otro lado a Spring, que nos va a ofrecer utilidades para trabajar con BBDD de manera sencilla y rápida.

Antiguamene, nos tocaba:
- Crear una BBDD
- Crear las tablas necesarias en la BBDD
- Crear scripts para insertar los datos en las tablas de la BBDD
- Que nuestro programa, cuando necesitase datos, lanzase consultas SQL a la BBDD para obtener los datos que necesitaba.

Hoy en día todo ese trabajo lo hace Spring por nosotros. No necesitamos tirar ni una sola línea de código SQL. 

Además, como tenemos estandarizado la forma de acceder a los diccionarios y sus poalabras (diccionarios-api), no vamos a tener que cambiar NADA de nuestro sistema (casi nada). Lo único que vamos a necesitar hacer es crear un proyecto nuevo: diccionarios-en-bbdd

Cuando tengamos ese proyecto acabado, lo que haremos será reemplazar en nuestro servicio web el componente diccionarios-en-ficheros por el componente diccionarios-en-bbdd. Y ya está. Todo lo demás seguirá funcionando igual.

## Cómo va a ser la estructura de mi BBDD.

Es decir, en otras palabras... que entidades/tablas vamos a tener en nuestra BBDD y qué relaciones van a tener entre ellas.

        Idiomas                 Palabras                           Significados
        | ID | codigo  |        | ID | palabra | idioma_id |       | ID | palabra_id | significado                                       |
        |----|---------|        |----|---------|-----------|       |----|------------|---------------------------------------------------|
        |  1 | es      |   -<   |  1 | melón   | 1         |  -<   |  1 | 1          | Fruta de color verde por fuera y rojo por dentro. |
        |  2 | en      |        |  2 | house   | 2         |       |  2 | 1          | Persona con pocas luces.                          |
        |  3 | elfico  |        |  3 | abanico | 1         |       |  3 | 4          | Persona con pocas luces.                          |
                                |  4 | cenutrio | 1        |

Desde el punto de vista del idioma:
                         Relación                           Relación
                        ONE-TO-MANY                         ONE-TO-MANY
                Un idioma tiene muchas palabras          Una palabra tiene muchos significados
Desde el punto de vista de las palabras:
                        MANY-TO-ONE                          MANY-TO-ONE
                Muchas palabras pertenecen a un idioma   Muchos significados pertenecen a una palabra


Por motivos de seguridad en los datos, no debería poder tener 2 idiomas con el mismo código. 
Por lo tanto, el campo código de la tabla Idiomas debería ser único.

Con las mismas, la combinación de los campos palabra e idioma_id de la tabla Palabras debería ser única. No debería poder tener 2 palabras iguales para el mismo idioma.
Si puede haber la misma palabra (escrita igual) en distintos idiomas, pero no puede haber la misma palabra (escrita igual) en el mismo idioma.

El mismo significado no puede estar asignado 2 veces a la misma palabra. Por lo tanto, la combinación de los campos palabra_id y significado de la tabla Significados debería ser única. No debería poder tener 2 significados iguales para la misma palabra.


Con esto por ahora es suficiente.

Antiguamente, una vez creado este modelo de bbdd, tendría que generar un script de creación de la BBDD y de las tablas, y otro script para insertar los datos en las tablas. Y luego tendría que ejecutar esos scripts en la BBDD para crearla y llenarla de datos.

```sql
CREATE TABLE Idiomas (
    ID INT PRIMARY KEY,
    codigo VARCHAR(10) NOT NULL UNIQUE
);
CREATE TABLE Palabras (
    ID INT PRIMARY KEY,
    palabra VARCHAR(100),
    idioma_id INT,
    UNIQUE (palabra, idioma_id),
    FOREIGN KEY (idioma_id) REFERENCES Idiomas(ID)
);

CREATE TABLE Significados (
    ID INT PRIMARY KEY,
    palabra_id INT,
    significado VARCHAR(255),
    UNIQUE (palabra_id, significado),
    FOREIGN KEY (palabra_id) REFERENCES Palabras(ID)
);

INSERT INTO Idiomas (ID, codigo) VALUES (1, 'es'), (2, 'en'), (3, 'elfico');
INSERT INTO Palabras (ID, palabra, idioma_id) VALUES
(1, 'melón', 1),
(2, 'house', 2),
(3, 'abanico', 1),
(4, 'cenutrio', 1);
INSERT INTO Significados (ID, palabra_id, significado) VALUES
(1, 1, 'Fruta de color verde por fuera y rojo por dentro.'),
(2, 1, 'Persona con pocas luces.'),
(3, 4, 'Persona con pocas luces.');
```

Este es un Script SQL que crea la BBDD y las tablas, y luego inserta los datos en las tablas.

Este trabajo (la creación de este script) es lo que me regala Spring.
De hecho quien realmente nos regala ese trabajo es una librería que hay por debajo de spring, llamada Hibernate. Hibernate es un ORM (Object Relational Mapping). Es decir, es una librería que nos permite mapear objetos JAVA a tablas de una BBDD y viceversa.

# Qué necesitamos hacer una vez tenemos el diseño de la BBDD

Hibernate junto con Spring, me imponen una forma de trabajar (ya dijimos que Spring es un framework, y los frameworks imponen una forma de trabajar).
Esto es bueno! Ya que da igual a la empresa / equipo que yo vaya, cualquier aplciación creada con Spring para manejar datos en una BBDD va a tener la misma estructura, y eso es bueno para los programadores, ya que nos permite movernos de un proyecto a otro sin tener que aprender nada nuevo.

Lo que necesitamos es entender como Spring obliga a crear la estructura de nuestro proyecto para trabajar con BBDD.
Spring e Hibernate utilizan / adoptan / Adaptan uno de los estándares definidos en JEE (Java Enterprise Edition) para trabajar con BBDD, llamado JPA (Java Persistence API).
JPA es parte del estandar JEE, y Spring e Hibernate lo utilizan para trabajar con BBDD Relacionales.

Lo primero que necesitaremos será crear clases que tengan los campos de las tablas de la BBDD. Es decir, necesitaremos crear 3 clases: Idioma, Palabra y Significado, con los datos que hay en las tablas de la BBDD. Y además, necesitaremos crear relaciones entre esas clases, que reflejen las relaciones que hay entre las tablas de la BBDD (al menos las relaciones que me interese representar).

Ahora bien... una cosa es cómo JAVA manipula los datos que voy a guardar en la BBDD, y otra cosa es cómo se guardan esos datos en la BBDD.
Será necesario ir añadiendo ANOTACIONES (nombrecitos de esos que empiezan por @...) en esas clases para indicarle a Spring/Hibernate cómo se guardan esos datos en la BBDD. 

Eso será lo primero.

Una veez hecho esto, necesitaremos crear en nuestro sistema un componente que se encargue de acceder a la BBDD para obtener los datos que necesitemos.
Es decir, UNA COSA SON LOS DATOS (las clases que representan los datos de la BBDD) y OTRA COSA ES EL ACCESO/OPERACIONES QUE PUEDO REALIZAR SOBRE LOS DATOS.
Ese componente es lo que Hibernate/Spring llaman un REPOSITORIO. Un repositorio es un componente que se encarga de acceder a la BBDD para obtener los datos que necesitemos. Cada Tipo de dato que gestione debe tener su propio repositorio. En nuestro caso:

    TIPO DE DATO        REPOSITORIOS
    Idioma              IdiomaRepository
    Palabra             PalabraRepository
    Significado         SignificadoRepository

PERO, aquí va a ocurrir algo mágico.
Spring, junto con Hibernate, nos regalan el código de esos repositorios. 
Nosotros definiremos un interfaz para cada repositorio, y Spring/Hibernate se encargarán de generar la clase que implemente esa interfaz.
Hasta ahora, nosotros hemos definido interfaces...
Pero luego hemos creado 1 o varias clases que implementan esas interfaces.

    Interfaces                      Clases
    InterfazGrafica                 InterfazGraficaTerminal
    Diccionario                     DiccionarioEnFicheros, DiccionarioEnServicioWeb, DiccionarioEnBBDD
    SuministadorDeDiccionarios      SuministradorDeDiccionariosEnFicheros, SuministradorDeDiccionariosEnServicioWeb, SuministradorDeDiccionariosEnBBDD

Con BBDD la cosa cambia. Nosotros SOLO definimos la interfaz, y Spring/Hibernate se encargan de generar la clase que implementa esa interfaz. Esto nos ahorra mucho código y mucho tiempo.

Cada una de las clases de los tipos de datos (ENTIDADES) tendrán los campos de la tabla como atributos de la clase privados... Y necesitamos métodos setter y getter para poder acceder a esos atributos desde fuera de la clase.
Por ahora yo he creado los atributos privados y los métodos getter y setter de las clases manualmente.
Más adelante os enseñaré una forma mucho más eficiente y práctica de añadir todos esos métodos setter y getter de manera automática, sin tener que escribirlos uno a uno. Va a ser gracias a una librería que existe en java y que usamos muchñisimo en la industria, llamada Lombok. Pero eso será más adelante.

En los archivos de Repositorio lo que definimos son las operaciones que podemos realizar sobre los datos de la BBDD.

Por ejemplo, sobre datos de tipo Idiomas quiero poder:
- guardarUnIdioma(Idioma idioma) -> Guardar un idioma en la BBDD
- obtenerTodosLosIdiomas() -> Obtener todos los idiomas de la BBDD
- obtenerIdiomaPorCodigo(String codigo) -> Obtener un idioma de la BBDD por su código
- obtenerIdiomaPorId(int id) -> Obtener un idioma de la BBDD por su id
- actualizarIdioma(Idioma idioma) -> Actualizar un idioma en la BBDD
- borrarIdioma(Idioma idioma) -> Borrar un idioma de la BBDD

Spring en automático ME REGALA MUCHAS DE ESAS OPERACIONES... las más comunes. 
A esas operaciones les asigna unos nombres concretos, que debo conocer.
Por ejemplo:
- crearlo un idioma                     repositorioIdioma.save(Idioma idioma)
- obtener todos los idiomas             repositorioIdioma.findAll()
- obtener un idioma por su id           repositorioIdioma.findById(int id)
- actualizar un idioma                  repositorioIdioma.save(Idioma idioma)
- borrar un idioma.                     repositorioIdioma.delete(Idioma idioma)
- existe un idioma con un id determinado?  repositorioIdioma.existsById(int id)

Y asi hasta 30 operaciones más.

Ahora bien, puede haber casos (y el nuestro es uno de ellos) en los que necesitemos operaciones que no están entre las operaciones que Spring/Hibernate nos regalan.
Lo único que vamos a definir en los archivos de Repositorio son las operaciones que no nos regala Spring/Hibernate y que necesitamos para nuestro sistema.

Por ejemplo, nuestros usuario no van a conocer los id de los idiomas, ni de las palabras, ni de los significados.
Conocerá el código de los idiomas por ejemplo.
Para recuperar un idioma, o saber si existe un idioma, no podemos usar la operación que nos regala Spring/Hibernate para obtener un idioma por su id, ya que no conocemos el id del idioma. 

Necesitamos una operación que nos permita obtener un idioma por su código o al menos saber si existe.
Esa operación la voy a definir yo. Pero voy a usar una nomenclatura concreta para que Spring/Hibernate entiendan la naturaleza de la operación que estoy definiendo y me la genere automáticamente (que escriba hibernate la query SQL que necesito para obtener el idioma por su código).

En este caso sería:

    boolean existsByCodigo(String codigo) -> Devuelve true si existe un idioma con el código indicado, false en caso contrario.
---

HEMOS TERMINADO EL TRABAJO CON LA BBDD.
Lo que queda es ahora exponer la funcionalidad de esos repositorios a través del api de gestion de diccionarios que habiamos definido.

---

JEE

Antiguamente se llamaba J2EE y significaba: Java 2 Enterprise Edition. 
Posteriormente se rebautizó como JEE, significando Jakarta Enterprise Edition.

A su vez, JEE es una colección de estándares que definen cómo debe ser una aplicación empresarial en JAVA.
Uno de ellos es el estándar JPA (Java Persistence API), que define cómo debe ser una aplicación empresarial en JAVA que trabaje con BBDD Relacionales.

