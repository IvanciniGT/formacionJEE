package com.curso.diccionarios.bbdd.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;    // Esta librería no es una librería estandar de JAVA... Sino es parte de JPA que es a su vez parte de JEE
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
// Spring, junto con hibernate me ofrecen una implementación de JPA para poder persistir objetos en la base de datos
                                      // Necesitamos en nuestro proyecto las dependencias de SPRING para trabajar con BBDD vía JPA.
                                      // Springboot (que dijimos incluía STARTERS) nos ofrece un starter para trabajar con JPA y BBDD, que es el starter spring-boot-starter-data-jpa
import jakarta.persistence.Table;

@Entity                     // Esta clase define un tipo de dato persistible en la base de datos
@Table(name = "idiomas")    // Los datos de este tipo queremos que se guarden en la tabla "idiomas" de la base de datos
public class Idioma {
    
    @Id                     // Este campo actua como identificador único de cada registro de la tabla "idiomas"
                            // Es lo que debe configurarse en la BBDD como PRIMARY KEY de la tabla "idiomas"
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Además, querremos que este campo sea autoincremental, es decir, que cada vez que se inserte un nuevo registro en la tabla "idiomas", el valor de este campo se genere automáticamente y sea único.
    private int id;

    @Column(name = "codigo", nullable = false, unique = true) // Este campo se guardará en la columna "codigo" de la tabla "idiomas", y no puede ser nulo ni repetirse
    private String codigo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

}
