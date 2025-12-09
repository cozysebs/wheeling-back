package com.lys.wheeling.service.game;

import com.lys.wheeling.dto.GameDTO;

import java.util.List;

public interface GameService {

    // 단일 게임 생성/업데이트 (slug 기준 upsert)   // upsert = update + insert
    GameDTO saveGame(GameDTO gameDTO);

    // 프론트의 gamesConfig 전체를 한 번에 동기화 (slug 기준 upsert)
    // React의 gamesConfig를 그대로 [GameDTO] 형태로 보내면, slug 기준으로 upsert하게 된다.
    List<GameDTO> syncGames(List<GameDTO> gameDTOs);

    // 조회 계열
    List<GameDTO> getAllGames();

    GameDTO getGameById(Long gameId);

    GameDTO getGameBySlug(String slug);

    void deleteGame(Long gameId);
}
