package com.aluracursos.literalura.service;

import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AutorService {
    private final AutorRepository autorRepository;

    @Autowired
    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public List<Autor> obtenerTodosLosAutores() {
        return autorRepository.findAll();
    }

    public List<Autor> obtenerAutoresVivosEnAno(int ano) {
        return autorRepository.findAll().stream()
                .filter(autor -> autor.getAnoNacimiento() != null && autor.getAnoNacimiento() <= ano &&
                        (autor.getAnoFallecimiento() == null || autor.getAnoFallecimiento() >= ano))
                .collect(Collectors.toList());
    }


    public Autor agregarAutor(Autor autor) {
        List<Autor> autoresExistentes = autorRepository.findAllByNombreAndAnoNacimientoAndAnoFallecimiento(
                autor.getNombre(), autor.getAnoNacimiento(), autor.getAnoFallecimiento());

        if (autoresExistentes.isEmpty()) {
            return autorRepository.save(autor);
        } else {
            // Si ya existen autores con la misma información, devolver el primero
            return autoresExistentes.get(0);
        }
    }
}
