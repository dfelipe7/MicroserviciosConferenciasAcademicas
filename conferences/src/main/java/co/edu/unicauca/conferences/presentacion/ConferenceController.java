package co.edu.unicauca.conferences.presentacion;

import co.edu.unicauca.conferences.dto.ArticleDTO;
import co.edu.unicauca.conferences.dto.UserDTO;
import co.edu.unicauca.conferences.model.Conference;
import co.edu.unicauca.conferences.servicio.ConferenceService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

/**
 * Controlador REST para gestionar las conferencias académicas. Proporciona
 * endpoints para crear, actualizar, eliminar y obtener conferencias. Este
 * controlador se comunica con el microservicio de sesión para validar el rol y
 * permisos del usuario, asegurando que solo los usuarios con rol de
 * "Organizador" puedan realizar ciertas operaciones.
 *
 *
 * @see ConferenceService
 * @see RestTemplate
 *
 * @author Jesus Iles
 * @author Daniel Muñoz
 * @author Esteban Martinez
 * @autor Felipe Armero
 */
@RestController
@RequestMapping("/api/conferences")
public class ConferenceController {

    @Autowired
    private ConferenceService conferenceService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${session.service.url}")
    private String sessionServiceUrl;

    /**
     * Endpoint para crear una nueva conferencia. Solo los usuarios con el rol
     * de "Organizador" pueden realizar esta operación.
     *
     * @param conference el objeto {@link Conference} con la información de la
     * conferencia a crear
     * @param userId ID del usuario que intenta crear la conferencia
     * @return una respuesta HTTP indicando el éxito o fracaso de la operación
     */
    @PostMapping
    public ResponseEntity<String> createConference(@RequestBody Conference conference, @RequestParam Long userId) {
        // Llamar al microservicio de sesión para obtener la información del usuario
        ResponseEntity<UserDTO> response = restTemplate.getForEntity(sessionServiceUrl + "/api/users/" + userId, UserDTO.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            UserDTO loggedUser = response.getBody();

            // Verificar que el usuario tenga el rol de organizador
            if (!"Organizador".equals(loggedUser.getRole())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado. Solo los organizadores pueden crear conferencias.");
            }

            // Asignar el userId como organizerId a la conferencia
            conference.setOrganizerId(userId);

            // Crear conferencia
            conferenceService.createConference(conference);
            return ResponseEntity.status(HttpStatus.CREATED).body("Conferencia creada con éxito.");
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Error al obtener información del usuario.");
        }
    }

    /**
     * Endpoint para obtener la lista de todas las conferencias.
     *
     * @return una lista de todas las conferencias
     */
    @GetMapping
    public ResponseEntity<List<Conference>> getAllConferences() {
        List<Conference> conferences = conferenceService.getAllConferences();
        return ResponseEntity.ok(conferences);
    }

    /**
     * Endpoint para obtener una conferencia específica por su ID.
     *
     * @param id ID de la conferencia a buscar
     * @return la conferencia si se encuentra, o un estado 404 si no se
     * encuentra
     */
    @GetMapping("/{id}")
    public ResponseEntity<Conference> getConferenceById(@PathVariable Long id) {
        Optional<Conference> conference = conferenceService.getConferenceById(id);
        return conference.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Endpoint para actualizar una conferencia existente. Solo los
     * organizadores pueden realizar esta operación y deben ser el creador de la
     * conferencia.
     *
     * @param id ID de la conferencia a actualizar
     * @param conference los nuevos datos de la conferencia
     * @param userId ID del usuario que intenta actualizar la conferencia
     * @return una respuesta HTTP indicando el éxito o fracaso de la operación
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateConference(@PathVariable Long id, @RequestBody Conference conference, @RequestParam Long userId) {
        ResponseEntity<UserDTO> response = restTemplate.getForEntity(sessionServiceUrl + "/api/users/" + userId, UserDTO.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            UserDTO loggedUser = response.getBody();

            if (!"Organizador".equals(loggedUser.getRole())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado. Solo los organizadores pueden actualizar conferencias.");
            }

            Optional<Conference> existingConference = conferenceService.getConferenceById(id);
            if (existingConference.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conferencia no encontrada.");
            }

            if (!existingConference.get().getOrganizerId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado. Solo el organizador que creó la conferencia puede actualizarla.");
            }

            conferenceService.updateConference(id, conference);
            return ResponseEntity.ok("Conferencia actualizada exitosamente.");
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Error al obtener información del usuario.");
        }
    }

    /**
     * Endpoint para eliminar una conferencia existente. Solo el organizador que
     * creó la conferencia puede eliminarla.
     *
     * @param id ID de la conferencia a eliminar
     * @param userId ID del usuario que intenta eliminar la conferencia
     * @return una respuesta HTTP indicando el éxito o fracaso de la operación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteConference(@PathVariable Long id, @RequestParam Long userId) {
        ResponseEntity<UserDTO> response = restTemplate.getForEntity(sessionServiceUrl + "/api/users/" + userId, UserDTO.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            UserDTO loggedUser = response.getBody();

            if (!"Organizador".equals(loggedUser.getRole())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado. Solo los organizadores pueden eliminar conferencias.");
            }

            Optional<Conference> existingConference = conferenceService.getConferenceById(id);
            if (existingConference.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conferencia no encontrada.");
            }

            if (!existingConference.get().getOrganizerId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado. Solo el organizador que creó la conferencia puede eliminarla.");
            }

            conferenceService.deleteConference(id);
            return ResponseEntity.ok("Conferencia eliminada exitosamente.");
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Error al obtener información del usuario.");
        }
    }

    @PostMapping("/{conferenceId}/articles/{articleId}/assign-evaluator")
    public ResponseEntity<String> assignEvaluator(
            @PathVariable Long conferenceId,
            @PathVariable Long articleId,
            @RequestParam Long evaluatorId) {

        // Validar que la conferencia exista
        Optional<Conference> conference = conferenceService.getConferenceById(conferenceId);
        if (conference.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conferencia no encontrada.");
        }

        // Llamar al microservicio de artículos para obtener la información del artículo
        ResponseEntity<ArticleDTO> articleResponse = restTemplate.getForEntity(
                "http://localhost:8086/api/articles/" + articleId, ArticleDTO.class); // Cambiar URL según la configuración
        if (!articleResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Artículo no encontrado.");
        }

        // Obtener el artículo desde la respuesta
        ArticleDTO article = articleResponse.getBody();

        // Verificar que el artículo pertenece a la conferencia
        if (!article.getConferenceId().equals(conferenceId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El artículo no pertenece a esta conferencia.");
        }

        // Simulación: Validar que el evaluador exista (este paso dependerá del microservicio de sesión)
        // Puedes reemplazar esto con una llamada real al microservicio de sesión para validar el evaluador.
        boolean evaluatorExists = true; // Simulación
        if (!evaluatorExists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Evaluador no encontrado.");
        }

        // Simulación de asignación (aquí deberías guardar la asignación en tu base de datos)
        System.out.println("Evaluador con ID " + evaluatorId + " asignado al artículo: " + article.getTitle());
        System.out.println("Archivo disponible en: " + article.getFilePath());

        // Respuesta exitosa
        return ResponseEntity.ok("Evaluador asignado con éxito al artículo.");
    }
    
    @GetMapping("/{conferenceId}/articles")
public ResponseEntity<List<ArticleDTO>> getArticlesByConference(@PathVariable Long conferenceId) {
    // Verificar que la conferencia exista
    Optional<Conference> conference = conferenceService.getConferenceById(conferenceId);
    if (conference.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // Llamar al microservicio de artículos para obtener los artículos de la conferencia
    ResponseEntity<ArticleDTO[]> response = restTemplate.getForEntity(
            "http://localhost:8086/api/articles/conference/" + conferenceId, ArticleDTO[].class);
    
    if (!response.getStatusCode().is2xxSuccessful()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    // Convertir el array de artículos a una lista
    ArticleDTO[] articlesArray = response.getBody();
    List<ArticleDTO> articles = List.of(articlesArray);

    return ResponseEntity.ok(articles);
}


}
