/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vistasAutor;

/**
 *
 * @author Unicauca
 */

import entidades.ArticleDTO;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;


public class AuthorService {

    private static AuthorService instance; // Instancia única de la clase

    private static final String BASE_URL = "http://localhost:8095/api/articles";
    private HttpClient client = HttpClient.newHttpClient();

    private AuthorService() {
        // Constructor privado para evitar instanciación directa
    }

    public static synchronized AuthorService getInstance() {
        if (instance == null) {
            instance = new AuthorService();
        }
        return instance;
    }

    public List<ArticleDTO> getArticlesByAuthor(String authorId) throws Exception {
        String url = BASE_URL + "/author/" + authorId;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new java.net.URI(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return new Gson().fromJson(response.body(), new TypeToken<List<ArticleDTO>>() {}.getType());
        } else {
            throw new Exception("Error al obtener artículos: " + response.body());
        }
    }
}
