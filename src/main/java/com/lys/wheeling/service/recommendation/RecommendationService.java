package com.lys.wheeling.service.recommendation;

import com.lys.wheeling.dto.GameDTO;

import java.util.List;

public interface RecommendationService {
    List<GameDTO> getRecommendations(Long viewerUserId);
}
