package vistasSesion;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Unicauca
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Servicio para interactuar con el backend de usuarios.
 */
public class UserService {

    private static final String BASE_URL = "http://localhost:8090/api/users";

    /**
     * Registra un nuevo usuario.
     *
     * @param name     Nombre del usuario.
     * @param email    Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @param role     Rol del usuario (ADMIN, AUTHOR, EVALUATOR).
     * @return Mensaje de éxito o error.
     */
    public String registerUser(String name, String email, String password, String role) {
        try {
            // Crear el objeto JSON
            JSONObject json = new JSONObject();
            json.put("name", name);
            json.put("email", email);
            json.put("password", password);
            json.put("role", role);

            // Configurar la conexión
            URL url = new URL(BASE_URL + "/register");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Enviar el JSON al servidor
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Leer la respuesta del servidor
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return "Registro exitoso: " + response.toString();
                }
            } else {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String errorLine;
                    while ((errorLine = br.readLine()) != null) {
                        errorResponse.append(errorLine.trim());
                    }
                    return "Error al registrar: " + errorResponse.toString();
                }
            }
        } catch (Exception e) {
            return "Error al registrar: " + e.getMessage();
        }
    }

    /**
     * Inicia sesión de un usuario.
     *
     * @param email    Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @return Mensaje de éxito o error.
     */
    public String login(String email, String password) {
        try {
            // Crear el objeto JSON
            JSONObject json = new JSONObject();
            json.put("email", email);
            json.put("password", password);

            // Configurar la conexión
            URL url = new URL(BASE_URL + "/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Enviar el JSON al servidor
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Leer la respuesta del servidor
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return "Inicio de sesión exitoso: " + response.toString();
                }
            } else {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String errorLine;
                    while ((errorLine = br.readLine()) != null) {
                        errorResponse.append(errorLine.trim());
                    }
                    return "Error al iniciar sesión: " + errorResponse.toString();
                }
            }
        } catch (Exception e) {
            return "Error al iniciar sesión: " + e.getMessage();
        }
    }



    public String getUserRole(String email) {
        try {
            URL url = new URL(BASE_URL + "/role?email=" + email);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            // Leer la respuesta
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return response.toString(); // Retorna el rol del usuario
                }
            } else {
                return "Error al obtener el rol del usuario, código de respuesta: " + responseCode;
            }
        } catch (IOException e) {
            return "Error al obtener el rol del usuario: " + e.getMessage();
        }
    }

    public String getUserId(String email) {
        try {
            URL url = new URL(BASE_URL + "/id?email=" + email);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            // Leer la respuesta
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return response.toString(); // Retorna el ID del usuario
                }
            } else {
                return "Error al obtener el ID del usuario, código de respuesta: " + responseCode;
            }
        } catch (IOException e) {
            return "Error al obtener el ID del usuario: " + e.getMessage();
        }
    }

}




