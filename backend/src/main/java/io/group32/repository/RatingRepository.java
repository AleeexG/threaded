package io.group32.repository;

import io.group32.model.Rating;
import io.group32.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("SELECT r FROM Rating r WHERE r.seller.id = :sellerId")
    List<Rating> findBySellerId(Long sellerId);

    Rating findByOrder(Order order);

    Optional<Rating> findByOrderId(Long orderId);

    @Query("SELECT COALESCE(AVG(r.score), 0) FROM Rating r WHERE r.seller.id = :sellerId")
    Double findAverageBySellerId(Long sellerId);

    @Query("SELECT COUNT(r) FROM Rating r WHERE r.seller.id = :sellerId")
    Long countBySellerId(Long sellerId);
}
