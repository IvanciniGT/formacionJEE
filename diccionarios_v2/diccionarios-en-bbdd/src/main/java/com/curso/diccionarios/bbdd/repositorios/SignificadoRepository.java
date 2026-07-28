package com.curso.diccionarios.bbdd.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.curso.diccionarios.bbdd.entidades.Significado;
//import java.util.List;
//import com.curso.diccionarios.bbdd.entidades.Palabra;   

public interface SignificadoRepository extends JpaRepository<Significado, Integer> {
    
    // En mi caso, me interesa obtener todos los significados de una palabra concreta,
    // por lo que voy a crear un método que me devuelva todos los significados de una palabra concreta. Para ello, voy a crear un método que me devuelva una lista de significados, y le voy a pasar como parámetro la palabra de la que quiero obtener los significados.
    //List<Significado> findByPalabra(Palabra palabra);
    // Podemos meter en la clase palabra directamente que me dé los significados de esa palabra, y así no hace falta este método. Pero lo dejo aquí para que veas que también se puede hacer así.
}
