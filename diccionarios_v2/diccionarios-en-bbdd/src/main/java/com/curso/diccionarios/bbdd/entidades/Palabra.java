package com.curso.diccionarios.bbdd.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
// La combinación palabra/idioma es única, es decir, no puede haber dos registros en la tabla "palabras" con la misma palabra y el mismo idioma. Esto se debe a que una palabra puede tener diferentes significados en diferentes idiomas, pero no puede tener diferentes significados en el mismo idioma.
@Table( 
    name = "palabras", 
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"palabra", "idioma_id"}) // En este caso, como la unicidad la da la combinaciñón de 2 campos, esta es la forma de definirla
        // Si solo fuera un campo el que debe ser único, se podría definir con @Column(unique = true) en el campo correspondiente
    }
)
public class Palabra {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "palabra", nullable = false) // Este campo se guardará en la columna "palabra" de la tabla "palabras"
    private String palabra;

    @OneToMany(mappedBy = "palabra") // Esta anotación indica que esta clase (Palabra) tiene una relación de uno a muchos con la clase Significado. Es decir, una palabra puede tener varios significados. El atributo mappedBy indica que la relación se define en la clase Significado, en el campo "palabra".
    private List<Significado> significados; // Una palabra puede tener varios significados, por lo que este campo es una lista de significados. Esta relación se define en la clase Significado, donde se indica que muchos Significados pueden pertenecer a una misma Palabra.

    
    //@Column(name = "idioma_id", nullable = false) // Este campo se guardará en la columna "idioma_id" de la tabla "palabras", y no puede ser nulo
    // Esta columna está REALACIONADA con otra columna que existe en otra tabla.
    @ManyToOne  // Esta anotación indica que esta clase (Palabra) tiene una relación de muchos a uno con la clase Idioma. 
                // Es decir, muchas palabras pueden pertenecer a un mismo idioma.
                // En este caso, cuando tenemos columnas con relaciones, NO SE USA LA ANOTACION @Column, sino que se usa la anotación @JoinColumn para indicar que esta columna está relacionada con otra columna de otra tabla.
    @JoinColumn(name = "idioma_id", nullable = false) // Esta anotación indica que la columna "idioma_id" de la tabla "palabras" está relacionada con la columna "id" de la tabla "idiomas", y no puede ser nula
    private Idioma idioma;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public List<Significado> getSignificados() {
        return significados;
    }

    public void setSignificados(List<Significado> significados) {
        this.significados = significados;
    }

}
