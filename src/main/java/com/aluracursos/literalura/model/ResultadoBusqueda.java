package com.aluracursos.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultadoBusqueda {
    @JsonAlias("results")
    private List<DatosLibro> resultados;

    // Getters y setters

    public List<DatosLibro> getResultados() {
        return resultados;
    }

    public void setResultados(List<DatosLibro> resultados) {
        this.resultados = resultados;
    }
}