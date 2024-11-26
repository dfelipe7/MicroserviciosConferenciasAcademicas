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
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public ReviewDTO createReview(ReviewDTO reviewDTO) {
    // Mapeo DTO a entidad
    Review review = new Review();
    review.setArticleId(reviewDTO.getArticleId());
    review.setEvaluatorId(reviewDTO.getEvaluatorId());
    review.setComments(reviewDTO.getComments());
    review.setStatus(reviewDTO.getStatus());

    // Guardado de la entidad en la base de datos
    Review savedReview = reviewRepository.save(review);

    // Mapeo de entidad guardada a DTO para devolver el resultado
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
}
