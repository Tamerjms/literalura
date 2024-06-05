package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNombreAndAnoNacimientoAndAnoFallecimiento(String nombre, Integer anoNacimiento, Integer anoFallecimiento);

    // Añadir una consulta que devuelva una lista para manejar casos donde puedan existir duplicados
    List<Autor> findAllByNombreAndAnoNacimientoAndAnoFallecimiento(String nombre, Integer anoNacimiento, Integer anoFallecimiento);
}


