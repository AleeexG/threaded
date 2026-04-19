package io.group32.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "ratings")
@Data
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User seller;

    @ManyToOne
    private User buyer;

    @OneToOne
    private Order order;

    private int score;

    @Column(columnDefinition = "TEXT")
    private String review;

    private LocalDateTime createdAt = LocalDateTime.now();
}
