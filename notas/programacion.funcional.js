function generar_saludo_formal(nombre) {
    return "Buenos días, " + nombre;
}

function generar_saludo_informal(nombre) {
    return "Hola, " + nombre;
}

function imprimir_doble(numero) {
    resultado = numero * 2;
    console.log(resultado);             // En JAVA: System.out.println(resultado);
}

imprimir_doble(5);
imprimir_doble(10);
// Hasta aqui lo que hemos escrito es programación PROCEDURAL

// Que es la programación funcional
miVariable = 33;                // Aquí tengo una variable que apunta al dato que está guardado en RAM 33
miVariable = imprimir_doble;    // Ahora miVariable apunta a la función imprimir_doble, que está guardada en RAM
                                // Trato la función como su fuera un dato más.
                                // No estoy ejecutando la función.
                                // Solo la referencio desde miVariable.
miVariable(5);                  // Ahora ejecuto la función a través de miVariable, que apunta a la función imprimir_doble
                                // Aquí SI ESTOY EJECUTANDO LA FUNCION imprimir_doble, porque estoy usando los paréntesis.

// Este es el concepto de programación funcional.
// Cuando el lenguaje me permite que una variable apunte a una función 
// y posteriormente ejecutar esa función a través de la variable decimos que el lenguaje 
// soporta el paradigma de programación funcional.

// El concepto es simple... A LA PAR que parece un poco inútil. Para qué querría yo que una variable apunte a una 
// función y luego ejecutar la función a través de la variable? Mejor directamente ejecuto la funció directamente.

// LA cuestión no es lo que ES la programación funcional. Sino lo que puedo hacer si el lenguaje soporta esto.

// Desde este momento puedo hacer 2 cosas:
// - Crear funciones que devuelvan otras funciones (CLOSURES)
// - Crear funciones que acepten como parámetro otras funciones.
//   Esto nos permite INYECTAR LOGICA A UNA FUNCION EN TIEMPO DE EJECUCION.

// LA función aceptaba un argumento: EL NUMERO QUE QUIERO DOBLAR.
// Y si quisiera que la función también permitiera que yo le inyectara la lógica de la OPERACION QUE DEBEN REALIZARSE
// DE FORMA QUE NO SIEMRPE SEA EL DOBLE?

function doblar(numero) {
    return numero * 2;
}

function triplicar(numero) {
    return numero * 3;
}

function mitad(numero) {
    return numero / 2;
}

function imprimir_resultado_de_operacion(numero, operacion) {
    resultado = operacion(numero);
    console.log(resultado);             // En JAVA: System.out.println(resultado);
}

imprimir_resultado_de_operacion(5, doblar);
imprimir_resultado_de_operacion(5, triplicar);
imprimir_resultado_de_operacion(5, mitad);
// Esto es distinto de:
// imprimir_resultado_de_operacion(mitad(5));
// En este caso de abajo, a la función imprimir_resultado_de_operacion le estoy pasando el
// resultado de la función mitad(5) que sería 2.5.
// Lo primero que se ejecutaría sería la función mitad 
// El resultado es lo que se pasa a la función imprimir_resultado_de_operacion, que se ejecutaría después.

// En el caso de nuestro ejemplo:
imprimir_resultado_de_operacion(5, mitad);
// Lo primero que se ejecuta es la función imprimir_resultado_de_operacion,
// que recibe como parámetro el número 5 y la función que debe ejecutarse para la operación.
// Cambia el orden de ejecución.

// Al ejecutar este programa, y ver la consola, veo un 15.
// Es el resultado de la función triplar(5)
// La pregunta es en QUE LINEA DE ESTE PROGRAMA SE HA EJECUTADO LA FUNCION TRIPLAR?
// Y esa función se ha ejecutado en la linea 52:
//    resultado = operacion(numero);

// RESUMEN:
// Una de las virtudes de la programación funcional es que permite INYECTAR LOGICA 
// en tiempo de ejecución.
// En ocasiones parte de la lógica de una función es DESCONOCIDA en el momento en que creo la función.
// Y Entonces hago que esa lógica se inyecte en tiempo de ejecución a través de una función que se pasa como parámetro.

// Habitualmente creamos funciones para:
// - Reutilizar código
// - Mejorar la legibilidad/estructura del código haciéndolo más mantenible
// Cuando hacemos uso de programación funcional, hay un tercer motivo para crear funciones: 
// - Por necesidad. A veces quiero / necesito llamar a una función, pero esa función requiere que le pase otra función como argumento.

function imprimir_saludo(nombre, funcion_generadora_de_saludo) {
    saludo = funcion_generadora_de_saludo(nombre);
    console.log(saludo);
}

imprimir_saludo("Juan", generar_saludo_formal);
imprimir_saludo("Menchu", generar_saludo_informal);
// Una pregunta sería.
// Para qué he creado la función generar_saludo_formal y generar_saludo_informal?
// - Puede ser que ese código (esa forma de generar saludos) no vaya a reusarla en ningún otro sitio de mi código
//              X Reutilizar código
// - Y resulta que el tener la función definida de forma tradicional NO ESTA APORTANDO LEGIBILIDAD NI ESTRUCTURA A MI CÓDIGO
// Es decir, cuando veo la linea 102, No tengo la menor idea de l que saldrá por pantalla.
// Para entenderlo, me toda ir a la linea donde está definida la función generar_saludo_formal y leerla.
//              X Mejorar la legibilidad/estructura del código haciéndolo más mantenible
// He creado la función por un único motivo:
// Quiero llamar a la función imprimir_saludo, pero esa función requiere que le pase otra función como argumento.

// En estos escenarios, cuando:
// - No quiero reutilizar código
// - El crear la función de forma tradicional no mejora la legibilidad/estructura del código 
// los lenguajes que soportan programación funcional, me permiten crear funciones de otra forma / con otra sintaxis.
// Es lo que llamamos EXPRESIONES LAMBDA

// Una expresión lambda es ante todo una expresión.
// En desarrollo llamamos EXPRESION a un fragmento de código que devuelve un valor.
var texto = "Hola mundo";        // Esto es un statement. Es una sentencia. No es una expresión. No devuelve un valor. Solo ejecuta una acción.
var texto = "Hola mundo".toUpperCase();  // Esto es otro statement.
            //////////////////////////     Dentro de este statement hay una expresión. La expresión es "Hola mundo".toUpperCase() que devuelve un valor. Ese valor es el que se asigna a la variable texto.
        
var numero = 33+7;    // Esto es otro statement. 
             ////  Dentro de este statement hay una expresión. La expresión es 33+7 que devuelve un valor. Ese valor es el que se asigna a la variable numero.

// Si una expresión es un trozo de código que devuelve un valor,
// una EXPRESION lambda es un trozo de código que devuelve un valor.
// Qué valor devuelve? Una función ANONIMA (sin nombre) definida dentro de la propia expresión.

//function generar_saludo_formal(nombre) {
//    return "Buenos días, " + nombre;
//}

// YA HEMOS CREADO UNA EXPRESION LAMBDA. 
// Estoy definiendo la misma función que antes, pero de otra forma.
// Sin ponerle un nombre, y poniendo una flecha => entre los parámetros y el cuerpo de la función.
// Esto es la sintaxis de JS, En JAVA cambiaría la => ->... en lugar de un IGUAL antes del mayor que sería un HGUION MEDIO seguido de un MAYOR QUE.
// Por ser una expresión, no solo crea la función, sino que se nos devuelve una referencia a la función...
// Puedo por ejemplo asociarla a una variable.
miNuevaFuncion = (nombre) => {
    return "Buenos días, " + nombre;
}

// Además, en casos simples como este, puedo obviar los () de los parámetros, y las llaves {} del cuerpo de la función, y el return, y dejarlo todo en una sola línea.
miNuevaFuncion = nombre => "Buenos días, " + nombre;
// Al principio es un SHOCK esta sintaxis. Con el tiempo se convierte en natural, y es muy agradecida.
// En esa linea estamos DEFINIENDO UNA FUNCION, igual que en las lineas 1-3 de nuestro fichero.
// Y ademas estableciendo una variable que apunta a esa función, igual que en la linea 20 de nuestro fichero.

// Esto incluso puedo hacerlo de otra forma, sin necesidad de variable EXPLICITA:
imprimir_saludo("Federico", nombre => "¿Qué tal, " + nombre+"?");
// nombre => "¿Qué tal, " + nombre+"?"  ESTOY DEFINIENDO UNA FUNCION QUE:
//                                      - Recibe un parámetro llamado nombre            
//                                      - Devuelve el texto "¿Qué tal, " + nombre+"?"

// Ahora tengo una sintaxis más compacta para definir funciones, 
// y que mejora la legibilidad de mi código cuando uso programación funcional
// Y creo funciones QUE NO QUIERO REUSAR pero que TENGO LA NECESIDAD DE CREAR PARA PODER INYECTAR LOGICA EN TIEMPO DE EJECUCION A OTRA FUNCION.

// Cuando veo la linea de código 155, tengo claro el texto que va a salir por pantalla
// Sin necesidad de ir a buscar la definición de la función en otra parte del código.

// Es una sintaxis muy compacta, pero hay que entenderla.
// Como regla, AYUDA MUCHO CADA VEZ QUE VEA una flecha => empezar diciendo:
//                  "ESTOY DEFINIENDO UNA FUNCION QUE..."