package io.group32.controller;

import io.group32.model.Rating;
import io.group32.service.RatingService;
import io.group32.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;
    private final SessionService sessionService;

    public RatingController(RatingService ratingService, SessionService sessionService) {
        this.ratingService = ratingService;
        this.sessionService = sessionService;
    }

    @PostMapping("/{orderId}")
    public Map<String, Object> rate(
            @PathVariable Long orderId,
            @RequestParam int score,
            @RequestParam(required = false) String review,
            HttpServletRequest request
    ) {
        Long buyerId = sessionService.getUser(request).getId();
        Rating rating = ratingService.createRating(orderId, score, review, buyerId);

        Map<String, Object> res = new HashMap<>();
        res.put("success", rating != null);
        res.put("data", rating);
        return res;
    }

    @GetMapping("/seller/{sellerId}")
    public Map<String, Object> getSellerRatings(@PathVariable Long sellerId) {
        Map<String, Object> res = new HashMap<>();
        res.put("ratings", ratingService.getRatingsForSeller(sellerId));
        res.put("average", ratingService.getAverage(sellerId));
        res.put("count", ratingService.getCount(sellerId));
        return res;
    }
}
