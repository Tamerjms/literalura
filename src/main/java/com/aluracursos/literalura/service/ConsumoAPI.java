package com.aluracursos.literalura.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ConsumoAPI {
    public String obtenerDatos(String url){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url)).build();
        HttpResponse<String> response = null;
        try{
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error al enviar la solicitud HTTP: " + e.getMessage());
        }
        String json = response.body();
        if (json == null || json.isEmpty()) {
            throw new RuntimeException("La respuesta de la API está vacía");
        }
        return json;
    }
}
