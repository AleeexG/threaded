package io.group32.service;

import io.group32.model.Order;
import io.group32.model.OrderStatus;
import io.group32.model.Rating;
import io.group32.repository.OrderRepository;
import io.group32.repository.RatingRepository;
import io.group32.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public RatingService(RatingRepository ratingRepository, OrderRepository orderRepository, UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public Rating createRating(Long orderId, int score, String review, Long buyerId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) return null;
        if (order.getStatus() != OrderStatus.RECEIVED) return null;
        if (!order.getBuyer().getId().equals(buyerId)) return null;

        Rating existing = ratingRepository.findByOrder(order);
        if (existing != null) return null;

        Rating rating = new Rating();
        rating.setOrder(order);
        rating.setBuyer(order.getBuyer());
        rating.setSeller(order.getListing().getUser());
        rating.setScore(score);
        rating.setReview(review);

        return ratingRepository.save(rating);
    }

    public List<Map<String, Object>> getRatingsForSeller(Long sellerId) {
        List<Rating> ratings = ratingRepository.findBySellerId(sellerId);
        List<Map<String, Object>> list = new ArrayList<>();

        for (Rating r : ratings) {
            Map<String, Object> map = new HashMap<>();
            map.put("score", r.getScore());
            map.put("review", r.getReview());
            map.put("createdAt", r.getCreatedAt());

            Map<String, Object> buyer = new HashMap<>();
            buyer.put("id", r.getBuyer().getId());
            buyer.put("username", r.getBuyer().getUsername());
            map.put("buyer", buyer);

            list.add(map);
        }

        return list;
    }

    public Double getAverage(Long sellerId) {
        return ratingRepository.findAverageBySellerId(sellerId);
    }

    public Long getCount(Long sellerId) {
        Long count = ratingRepository.countBySellerId(sellerId);
        return count == null ? 0L : count;
    }
}
