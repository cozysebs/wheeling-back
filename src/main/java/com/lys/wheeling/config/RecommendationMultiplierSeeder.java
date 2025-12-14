package com.lys.wheeling.config;

import com.lys.wheeling.domain.RecommendationMultiplier;
import com.lys.wheeling.repository.RecommendationMultiplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RecommendationMultiplierSeeder implements CommandLineRunner {

    private final RecommendationMultiplierRepository repo;

    @Override
    @Transactional
    public void run(String... args) {
        if (repo.count() > 0) return;

        // rank 1..10 : 2.0, 1.9, ... , 1.1
        for (int rank = 1; rank <= 10; rank++) {
            double multiplier = 2.1 - (0.1 * rank);
            repo.save(RecommendationMultiplier.builder()
                    .rankNo(rank)
                    .multiplier(multiplier)
                    .build());
        }
    }
}
