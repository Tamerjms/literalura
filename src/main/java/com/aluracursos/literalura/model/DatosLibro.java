package com.aluracursos.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(@JsonAlias("title") String titulo,
                         @JsonAlias("subjects") List<String> categoria,
                         @JsonAlias("authors") List<DatosAutor> autores,
                         @JsonAlias("languages") List<String> lenguajes,
                         @JsonAlias("download_count") Integer descargas) {
}

