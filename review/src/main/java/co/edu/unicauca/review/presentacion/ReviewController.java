/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.unicauca.review.presentacion;

import co.edu.unicauca.review.dto.ReviewDTO;
import co.edu.unicauca.review.dto.UserDTO;
import co.edu.unicauca.review.servicio.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    
     @Autowired
    private RestTemplate restTemplate;

    @Value("${session.service.url}")
    private String sessionServiceUrl;

 @PostMapping
public ResponseEntity<String> createReview(@RequestBody ReviewDTO reviewDTO, @RequestParam Long reviewerId) {
    // Llamar al microservicio de sesión para obtener la información del usuario
    ResponseEntity<UserDTO> response = restTemplate.getForEntity(sessionServiceUrl + "/api/users/" + reviewerId, UserDTO.class);

    if (response.getStatusCode().is2xxSuccessful()) {
        UserDTO loggedUser = response.getBody();

        // Verificar que el usuario tenga el rol de evaluador
        if (!"Evaluador".equals(loggedUser.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado. Solo los evaluadores pueden crear revisiones.");
        }

        // Asignar el reviewerId al reviewDTO
        reviewDTO.setEvaluatorId(reviewerId);

        // Crear la revisión
        ReviewDTO createdReview = reviewService.createReview(reviewDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Revisión creada con éxito.");
    } else {
        return ResponseEntity.status(response.getStatusCode()).body("Error al obtener información del usuario.");
    }
}


    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        List<ReviewDTO> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long id) {
        Optional<ReviewDTO> reviewDTO = reviewService.getReviewById(id);
        return reviewDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
