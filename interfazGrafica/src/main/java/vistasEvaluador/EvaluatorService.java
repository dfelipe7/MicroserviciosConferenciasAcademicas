/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vistasEvaluador;

/**
 *
 * @author Unicauca
 */

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entidades.ReviewDTO;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class EvaluatorService {

    private static final String BASE_URL = "http://localhost:8070/api/reviews";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    // Obtener revisiones por evaluador
    public List<ReviewDTO> getReviewsByEvaluator(String evaluatorId) throws Exception {
        String url = BASE_URL + "/evaluator/" + evaluatorId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(), new TypeToken<List<ReviewDTO>>() {}.getType());
        } else {
            throw new Exception("Error al obtener revisiones: " + response.body());
        }
    }

    // Actualizar estado y comentarios de la revisión
    public String updateReview(ReviewDTO review) throws Exception {
        String url = BASE_URL + "/" + review.getId();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(review)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return "Revisión actualizada con éxito.";
        } else {
            throw new Exception("Error al actualizar revisión: " + response.body());
        }
    }
    
    public String updateReview(Long reviewId, String status, String comments) throws Exception {
    String url = "http://localhost:8070/api/reviews/" + reviewId;

    String json = String.format("{\"status\": \"%s\", \"comments\": \"%s\"}", status, comments);

    HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(url))
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(json))
            .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == 200) {
        return "Revisión actualizada con éxito.";
    } else {
        throw new Exception("Error al actualizar la revisión: " + response.body());
    }
}

}
