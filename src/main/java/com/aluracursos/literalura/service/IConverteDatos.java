package com.aluracursos.literalura.service;

public interface IConverteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
