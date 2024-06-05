package com.aluracursos.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Embeddable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
public record DatosAutor(
        @JsonAlias("name") String nombre,
        @JsonAlias("birth_year") Integer anoNacimiento,
        @JsonAlias("death_year") Integer anoFallecimiento) {}



