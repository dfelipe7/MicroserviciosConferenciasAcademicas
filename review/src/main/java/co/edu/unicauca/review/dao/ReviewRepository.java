/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package co.edu.unicauca.review.dao;

/**
 *
 * @author Unicauca
 */

import co.edu.unicauca.review.model.Review;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // Se pueden agregar métodos personalizados aquí si es necesario
    
    List<Review> findByEvaluatorId(Long evaluatorId);
    //List<Review> findByArticleIdIn(List<Long> articleIds);
    List<Review> findByArticleIdIn(List<Long> articleIds);

Optional<Review> findByEvaluatorIdAndArticleId(Long evaluatorId, Long articleId);
    Optional<Review> findByArticleId(Long articleId);

}
