package com.lys.wheeling.service.recommendation;

import com.lys.wheeling.domain.Game;
import com.lys.wheeling.domain.Post;
import com.lys.wheeling.domain.User;
import com.lys.wheeling.domain.elist.Category;
import com.lys.wheeling.dto.GameDTO;
import com.lys.wheeling.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecommendationServiceImpl implements RecommendationService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final RecommendationMultiplierRepository multiplierRepository;

    @Override
    public List<GameDTO> getRecommendations(Long viewerUserId) {

        User viewer = null;
        if (viewerUserId != null) {
            viewer = userRepository.findById(viewerUserId).orElse(null);
        }

        // rankNo -> multiplier
        Map<Integer, Double> multiplierMap = multiplierRepository.findAll().stream()
                .collect(Collectors.toMap(
                        m -> m.getRankNo(),
                        m -> m.getMultiplier()
                ));

        List<Category> prefs = (viewer != null && viewer.getFavoriteCategories() != null)
                ? viewer.getFavoriteCategories()
                : Collections.emptyList();

        // score 계산 후 정렬
        return gameRepository.findAll().stream()
                .map(game -> new AbstractMap.SimpleEntry<>(game, calculateFinalScore(game, prefs, multiplierMap)))
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .map(entry -> toGameDTO(entry.getKey()))
                .toList();
    }

    private double calculateFinalScore(Game game, List<Category> prefs, Map<Integer, Double> multiplierMap) {
        Post post = game.getPost();
        long likeCount = 0L;
        long bookmarkCount = 0L;

        if (post != null) {
            likeCount = likeRepository.countByPost(post);
            bookmarkCount = bookmarkRepository.countByPost(post);
        }

        double base = (likeCount * 1.0) + (bookmarkCount * 1.5);

        double mult = resolveMultiplier(game.getCategory(), prefs, multiplierMap);
        return base * mult;
    }

    private double resolveMultiplier(Category gameCategory, List<Category> prefs, Map<Integer, Double> multiplierMap) {
        if (gameCategory == null || prefs == null || prefs.isEmpty()) return 1.0;

        int idx = prefs.indexOf(gameCategory);
        if (idx < 0) return 1.0;

        int rank = idx + 1;     // 1순위 = index0
        if (rank > 10) return 1.0;

        // DB 테이블 우선, 없으면 기본 공식으로 fallback
        return multiplierMap.getOrDefault(rank, 2.1 - (0.1 * rank));
    }

    private GameDTO toGameDTO(Game game) {
        return GameDTO.builder()
                .gameId(game.getGameId())
                .slug(game.getSlug())
                .title(game.getTitle())
                .description(game.getDescription())
                .component(game.getComponent())
                .thumbnailUrl(game.getThumbnailUrl())
                .category(game.getCategory())
                .difficulty(game.getDifficulty())
                .build();
    }
}
