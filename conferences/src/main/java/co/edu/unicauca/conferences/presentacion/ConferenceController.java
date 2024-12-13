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
    
    @Value("${article.service.url}")
    private String articleServiceUrl;

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
    
    @GetMapping("/filter")
    public ResponseEntity<List<Conference>> filterConferences(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String topic) {
        List<Conference> filteredConferences = conferenceService.filterConferences(location, topic);
        return ResponseEntity.ok(filteredConferences);
    }

   

}
