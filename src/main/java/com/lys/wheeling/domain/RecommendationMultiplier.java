package com.lys.wheeling.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "recommendation_multiplier")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationMultiplier {

    // 1~10 (rank 자체를 PK로)
    @Id
    @Column(name = "rank_no")
    private Integer rankNo;

    @Column(nullable = false)
    private Double multiplier;
}
