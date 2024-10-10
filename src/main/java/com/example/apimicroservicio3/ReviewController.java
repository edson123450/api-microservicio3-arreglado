package com.example.apimicroservicio3;

import com.example.apimicroservicio3.Review;
import com.example.apimicroservicio3.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ReviewService reviewService;

    // Obtener todas las reviews
    @GetMapping("/all")
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // Obtener reviews por bookId y autor
    @GetMapping("/by-book-author")
    public List<ReviewDTO> getReviewsByBookAndAuthor(@RequestParam String title, @RequestParam String authorName) {
        return reviewService.getReviewsByBookAndAuthor(title, authorName);
    }

    // Obtener reviews por rating
    @GetMapping("/by-rating")
    public List<BookDetailsDTO> getReviewsByRating(@RequestParam int rating) {
        return reviewService.getReviewsByRating(rating);
    }

    @PostMapping("/new")
    public ResponseEntity<Map<String, String>> addNewReview(@RequestBody Review review) {
        reviewService.saveReview(review);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Review saved successfully");
        return ResponseEntity.status(201).body(response);
    }

    // Nueva API: Verificar si hay una review por book_id, author_id y user_id
    @GetMapping("/check-review")
    public ResponseEntity<Map<String, Object>> checkReview(@RequestParam int bookId, @RequestParam int authorId, @RequestParam int userId) {
        Optional<Review> reviewOpt = reviewRepository.findByBookIdAndAuthorIdAndUserId(bookId, authorId, userId);
        Map<String, Object> response = new HashMap<>();

        if (reviewOpt.isPresent()) {
            Review review = reviewOpt.get();
            // Si la review existe, devolver message = "si" y los datos de la review
            response.put("message", "si");
            response.put("rating", review.getRating());
            response.put("comment", review.getComment());
            return ResponseEntity.ok(response);  // Devuelve el JSON con los datos de la review
        } else {
            // Si no se encuentra la review, devolver message = "no" y valores por defecto
            response.put("message", "no");
            response.put("rating", 0);
            response.put("comment", "-no existe comentario-");  // Valor predeterminado más explícito
            return ResponseEntity.ok(response);  // Devuelve el JSON con valores por defecto
        }
    }
}