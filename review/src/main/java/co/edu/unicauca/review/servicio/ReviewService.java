/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.unicauca.review.servicio;

import co.edu.unicauca.review.dao.ReviewRepository;
import co.edu.unicauca.review.dto.ReviewDTO;
import co.edu.unicauca.review.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

public ReviewDTO createReview(ReviewDTO reviewDTO) {
    // Verificar si ya existe una revisión para el artículo y evaluador
    Optional<Review> existingReview = reviewRepository.findByEvaluatorIdAndArticleId(
            reviewDTO.getEvaluatorId(), reviewDTO.getArticleId());

    if (existingReview.isPresent()) {
        throw new IllegalArgumentException("Ya existe una revisión para este artículo y evaluador.");
    }

    // Mapeo DTO a entidad
    Review review = new Review();
    review.setArticleId(reviewDTO.getArticleId());
    review.setEvaluatorId(reviewDTO.getEvaluatorId());
    review.setComments(reviewDTO.getComments() != null ? reviewDTO.getComments() : "");
    review.setStatus(reviewDTO.getStatus() != null ? reviewDTO.getStatus() : "Pendiente");

    // Guardado en la base de datos
    Review savedReview = reviewRepository.save(review);

    // Retornar el DTO actualizado
    reviewDTO.setId(savedReview.getId());
    return reviewDTO;
}


    public List<ReviewDTO> getAllReviews() {
        // Implementar lógica para convertir cada Review a ReviewDTO
        return reviewRepository.findAll().stream().map(review -> {
            ReviewDTO dto = new ReviewDTO();
            dto.setId(review.getId());
            dto.setArticleId(review.getArticleId());
            dto.setEvaluatorId(review.getEvaluatorId());
            dto.setComments(review.getComments());
            dto.setStatus(review.getStatus());
            return dto;
        }).toList();
    }

    public Optional<ReviewDTO> getReviewById(Long id) {
        return reviewRepository.findById(id).map(review -> {
            ReviewDTO dto = new ReviewDTO();
            dto.setId(review.getId());
            dto.setArticleId(review.getArticleId());
            dto.setEvaluatorId(review.getEvaluatorId());
            dto.setComments(review.getComments());
            dto.setStatus(review.getStatus());
            return dto;
        });
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
    
    public List<ReviewDTO> getReviewsByEvaluator(Long evaluatorId) {
    return reviewRepository.findByEvaluatorId(evaluatorId).stream().map(review -> {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setArticleId(review.getArticleId());
        dto.setEvaluatorId(review.getEvaluatorId());
        dto.setComments(review.getComments());
        dto.setStatus(review.getStatus());
        return dto;
    }).toList();
}
    
     @Autowired
    private RestTemplate restTemplate;

    @Value("${article.service.url}")
    private String articleServiceUrl;

    /**
     * Obtiene las revisiones asociadas a un evaluador, combinando datos del
     * microservicio de artículos y revisiones locales.
     *
     * @param evaluatorId ID del evaluador.
     * @return Lista de ReviewDTO con datos combinados.
     * @throws Exception Si ocurre un error al consultar el microservicio de artículos.
     */
public List<ReviewDTO> getReviewsByEvaluatorWithArticles(Long evaluatorId) throws Exception {
    // Paso 1: Llamar al microservicio de artículos para obtener los artículos asignados
    String url = articleServiceUrl + "/api/articles/evaluator/" + evaluatorId;

    ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Map<String, Object>>>() {}
    );

    if (!response.getStatusCode().is2xxSuccessful()) {
        throw new Exception("Error al obtener artículos desde el microservicio de artículos");
    }

    List<Map<String, Object>> articles = response.getBody();

    if (articles == null || articles.isEmpty()) {
        return List.of(); // No hay artículos asignados
    }

    // Paso 2: Extraer IDs de los artículos
    List<Long> articleIds = articles.stream()
            .map(article -> Long.parseLong(article.get("id").toString()))
            .toList();

    // Paso 3: Consultar revisiones existentes en la base de datos
    List<Review> existingReviews = reviewRepository.findByArticleIdIn(articleIds);

    // Paso 4: Crear revisiones en la base de datos para los artículos que no tienen revisión
    for (Map<String, Object> article : articles) {
        Long articleId = Long.parseLong(article.get("id").toString());
        if (existingReviews.stream().noneMatch(review -> review.getArticleId().equals(articleId))) {
            // Crear una nueva revisión
            Review newReview = new Review();
            newReview.setArticleId(articleId);
            newReview.setEvaluatorId(evaluatorId);
            newReview.setStatus("Pendiente"); // Estado inicial
            newReview.setComments(""); // Comentarios vacíos inicialmente

            // Guardar la nueva revisión en la base de datos
            reviewRepository.save(newReview);
        }
    }

    // Paso 5: Consultar nuevamente las revisiones actualizadas
    List<Review> updatedReviews = reviewRepository.findByEvaluatorId(evaluatorId);

    // Paso 6: Mapear datos combinados a ReviewDTO
    return updatedReviews.stream().map(review -> {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setArticleId(review.getArticleId());
        dto.setEvaluatorId(review.getEvaluatorId());
        dto.setStatus(review.getStatus());
        dto.setComments(review.getComments());

        // Agregar datos del artículo
        Map<String, Object> article = articles.stream()
                .filter(a -> Long.valueOf(a.get("id").toString()).equals(review.getArticleId()))
                .findFirst()
                .orElse(null);

        if (article != null) {
            dto.setArticleName((String) article.get("name"));
            dto.setPdfPath((String) article.get("filePath"));
        }

        return dto;
    }).toList();
}


    public List<Map<String, Object>> getArticlesByEvaluator(Long evaluatorId) throws Exception {
    String url = articleServiceUrl + "/api/articles/evaluator/" + evaluatorId;
    ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Map<String, Object>>>() {}
    );

    if (!response.getStatusCode().is2xxSuccessful()) {
        throw new Exception("Error al obtener artículos desde el microservicio de artículos");
    }

    return response.getBody();
}
    
    public boolean updateReview(Long reviewId, ReviewDTO reviewDTO) {
    Optional<Review> optionalReview = reviewRepository.findById(reviewId);

    if (optionalReview.isPresent()) {
        Review review = optionalReview.get();

        // Actualizar campos
        review.setStatus(reviewDTO.getStatus());
        review.setComments(reviewDTO.getComments());

        // Guardar cambios
        reviewRepository.save(review);
        return true;
    } else {
        return false; // No se encontró la revisión
    }
}
    ////nuevo
    public Optional<ReviewDTO> getReviewByArticleId(Long articleId) {
        Optional<Review> review = reviewRepository.findByArticleId(articleId);

        if (review.isPresent()) {
            ReviewDTO dto = new ReviewDTO();
            dto.setId(review.get().getId());
            dto.setArticleId(review.get().getArticleId());
            dto.setStatus(review.get().getStatus());
            dto.setComments(review.get().getComments());
            return Optional.of(dto);
        }
        return Optional.empty();
    }



}
