package vistasAutor;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Unicauca
 */
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.net.ssl.HttpsURLConnection;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entidades.ArticleDTO;
import entidades.ReviewDTO;
import java.util.ArrayList;
import java.util.Map;
import utils.Observer;

public class ArticleService {

    private static ArticleService instance; // Instancia única de la clase

    private static final String BASE_URL = "http://localhost:8095/api/articles";
    private static final String CONFERENCES_URL = "http://localhost:8085/api/conferences";
    private static final String REVIEW_SERVICE_URL = "http://localhost:8070/api/reviews/article/";
    private HttpClient client = HttpClient.newHttpClient();
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Observer> observers = new ArrayList<>();

    private ArticleService() {
        // Constructor privado para evitar instanciación directa
    }

    public static synchronized ArticleService getInstance() {
        if (instance == null) {
            instance = new ArticleService();
        }
        return instance;
    }
    // Métodos para gestionar observadores
    // Métodos del patrón Observer
    public synchronized void addObserver(Observer observer) {
        if (!observers.contains(observer)) { // Evitar duplicados
            observers.add(observer);
        }
    }

    public synchronized void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public synchronized void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
   public void createArticle(String name, String summary, String keywords, String conferenceId, String userId, String filePath) throws Exception {
    // Crear el JSON del artículo con los campos correctos
    Map<String, Object> articleData = Map.of(
            "name", name,
            "summary", summary,
            "keywords", keywords,
            "filePath", filePath
    );

    String json = objectMapper.writeValueAsString(articleData);

    System.out.println("JSON a enviar: " + json);

    // Modificar la URL para incluir userId y conferenceId como parámetros de consulta
    String urlWithParams = String.format("%s?userId=%s&conferenceId=%s", BASE_URL, userId, conferenceId);

    System.out.println("URL con parámetros: " + urlWithParams);

    // Crear la solicitud HTTP
    HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(urlWithParams))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
            .build();

    // Enviar la solicitud y obtener la respuesta
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    System.out.println("Código de respuesta: " + response.statusCode());
    System.out.println("Respuesta del servidor: " + response.body());

    if (response.statusCode() == 201) {
        notifyObservers("Artículo creado: " + name);
    } else {
        throw new Exception("Error al crear el artículo: " + response.body());
    }
}



    // Método para obtener las conferencias
    public String[][] getConferences() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(CONFERENCES_URL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonElement jsonElement = JsonParser.parseString(response.body());
            JsonArray jsonArray = jsonElement.getAsJsonArray();

            // Aumenta el tamaño a 6 columnas para incluir el ID
            String[][] data = new String[jsonArray.size()][2];

            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement element = jsonArray.get(i);

                // Incluye el ID en la primera columna
                data[i][0] = element.getAsJsonObject().get("id").getAsString();
                data[i][1] = element.getAsJsonObject().get("name").getAsString();
            }

            return data;
        } else {
            throw new Exception("Error al obtener las conferencias: " + response.body());
        }
    }
    
    
   public String[][] getArticles() throws Exception {
    HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(BASE_URL))
            .GET()
            .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == 200) {
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        // Ajusta el tamaño de la matriz de acuerdo a los campos que necesitas mostrar
        String[][] data = new String[jsonArray.size()][6];

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonElement element = jsonArray.get(i);

            data[i][0] = element.getAsJsonObject().get("id").getAsString();
            data[i][1] = element.getAsJsonObject().get("name").getAsString(); // "name" en lugar de "title"
            data[i][2] = element.getAsJsonObject().get("summary").getAsString(); // "summary" en lugar de "abstract"
            data[i][3] = element.getAsJsonObject().get("keywords").getAsString();
            data[i][4] = element.getAsJsonObject().get("filePath").getAsString();
            data[i][5] = element.getAsJsonObject().get("autorId").getAsString(); // "autorId" en lugar de "authorId"
        }

        return data;
    } else {
        throw new Exception("Error al obtener los artículos: " + response.body());
    }
}


public void updateArticle(Long articleId, String title, String abstractText, String keywords, String pdfFilePath, String userId) throws Exception {
    // Reemplazar barras invertidas en filePath
    if (pdfFilePath != null) {
        pdfFilePath = pdfFilePath.replace("\\", "\\\\");
    }

    String json = String.format("{\"name\": \"%s\", \"summary\": \"%s\", \"keywords\": \"%s\", \"filePath\": \"%s\"}",
            title, abstractText, keywords, pdfFilePath);

    // Construir URL con parámetros
    String urlWithParams = String.format("%s/%d?userId=%s", BASE_URL, articleId, userId);

    System.out.println("URL construida: " + urlWithParams);
    System.out.println("JSON enviado: " + json);

    HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(urlWithParams))
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
            .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    System.out.println("Código de respuesta: " + response.statusCode());
    System.out.println("Respuesta del servidor: " + response.body());

    if (response.statusCode() == 200) {
        notifyObservers("Artículo actualizado: " + title);
    } else {
        throw new Exception("Error al actualizar el artículo: " + response.body());
    }
}



public void deleteArticle(Long articleId, String userId) throws Exception {
    // URL que incluye el userId como parámetro de consulta
    String urlWithParams = String.format("%s/%d?userId=%s", BASE_URL, articleId, userId);

    HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(urlWithParams))
            .DELETE()
            .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == 200) {
        notifyObservers("Artículo eliminado con ID: " + articleId);
    } else {
        throw new Exception("Error al eliminar el artículo: " + response.body());
    }
}
////nuevo

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
    ////
    

    public ReviewDTO getReviewByArticleId(Long articleId) throws Exception {
        String url = REVIEW_SERVICE_URL + articleId;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Gson gson = new Gson();
            return gson.fromJson(response.body(), ReviewDTO.class);
        } else if (response.statusCode() == 404) {
            return null; // No se encontró la revisión
        } else {
            throw new Exception("Error al obtener la revisión: " + response.body());
        }
    }
}

