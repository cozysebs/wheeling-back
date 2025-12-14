package com.lys.wheeling.controller;

import com.lys.wheeling.dto.GameDTO;
import com.lys.wheeling.security.UserPrincipal;
import com.lys.wheeling.service.game.GameService;
import com.lys.wheeling.service.recommendation.RecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/api/games")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GameController {

    private final GameService gameService;
    private final RecommendationService recommendationService;

    // 추천 게임 로드
    @GetMapping("/recommendations")
    public ResponseEntity<List<GameDTO>> getRecommendations( @AuthenticationPrincipal
                                                                 UserPrincipal principal) {
        Long viewerUserId = (principal != null) ? principal.getUserId() : null;
        return ResponseEntity.ok(recommendationService.getRecommendations(viewerUserId));
    }

    // 프론트의 gamesConfig 전체 동기화용. React에서 gamesConfig 배열을 그대로 POST
    @PostMapping("/sync")
    public ResponseEntity<List<GameDTO>> syncGames(@RequestBody List<GameDTO> games) {
        log.info("Syncing games, size={}", (games != null ? games.size() : 0));
        List<GameDTO> synced = gameService.syncGames(games);
        return ResponseEntity.ok(synced);
    }

    // 단일 생성/업데이트 (slug 기반 upsert)
    @PostMapping
    public ResponseEntity<GameDTO> saveGame(@RequestBody GameDTO dto) {
        GameDTO saved = gameService.saveGame(dto);
        return ResponseEntity.ok(saved);
    }

    // 전체 목록 조회
    @GetMapping
    public ResponseEntity<List<GameDTO>> getAllGames() {
        return ResponseEntity.ok(gameService.getAllGames());
    }

    // slug 기준 상세 조회
    @GetMapping("/{slug}")
    public ResponseEntity<GameDTO> getGameBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(gameService.getGameBySlug(slug));
    }

    // id 기준 상세 조회가 필요하면 해당 엔드포인트도 사용
    @GetMapping("/id/{gameId}")
    public ResponseEntity<GameDTO> getGameById(@PathVariable Long gameId) {
        return ResponseEntity.ok(gameService.getGameById(gameId));
    }

    // 삭제
    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long gameId) {
        gameService.deleteGame(gameId);
        return ResponseEntity.noContent().build();
    }
}
