package com.lys.wheeling.service.game;

import com.lys.wheeling.domain.Game;
import com.lys.wheeling.dto.GameDTO;
import com.lys.wheeling.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    // DTO <-> Entity 매핑 유틸
    private GameDTO entityToDTO(Game game) {
        if (game == null) { return null; }

        return GameDTO.builder()
                .gameId(game.getGameId())
                .slug(game.getSlug())
                .title(game.getTitle())
                .description(game.getDescription())
                .componentKey(game.getComponentKey())
                .thumbnailUrl(game.getThumbnailUrl())
                .category(game.getCategory())
                .difficulty(game.getDifficulty())
                .build();
    }

    // slug 기준 upsert에서 사용한다. 신규 Game인 경우 builder로 생성, 기존 Game인 경우 필드만 업데이트.
    private Game applyDTOToEntity(GameDTO dto, Game base) {
        Game target = (base != null) ? base : new Game();

        target.setSlug(dto.getSlug());
        target.setTitle(dto.getTitle());
        target.setDescription(dto.getDescription());
        target.setComponentKey(dto.getComponentKey());
        target.setThumbnailUrl(dto.getThumbnailUrl());
        target.setCategory(dto.getCategory());
        target.setDifficulty(dto.getDifficulty());

        return target;
    }

    // 단일 저장 (slug 기준 upsert)
    @Override
    public GameDTO saveGame(GameDTO dto) {
        if(dto.getSlug() == null || dto.getSlug().isBlank()){
            throw new IllegalArgumentException("slug is required");
        }
        Game existing = gameRepository.findBySlug(dto.getSlug()).orElse(null);
        Game toSave = applyDTOToEntity(dto, existing);

        Game saved = gameRepository.save(toSave);
        log.info("Saved game: slug={}, id={}", saved.getSlug(), saved.getGameId());
        return entityToDTO(saved);
    }

    // gameConfig 전체 동기화
    @Override
    public List<GameDTO> syncGames(List<GameDTO> gameDTOs) {
        if(gameDTOs == null || gameDTOs.isEmpty()){
            return List.of();
        }
        return gameDTOs.stream()
                .map(this::saveGame)
                .collect(Collectors.toList());
    }

    // 조회 계열

    @Override
    @Transactional(readOnly = true)
    public List<GameDTO> getAllGames() {
        return gameRepository.findAll()
                .stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public GameDTO getGameById(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(()-> new NoSuchElementException("Game not found: id=" + gameId));
        return entityToDTO(game);
    }

    @Override
    @Transactional(readOnly = true)
    public GameDTO getGameBySlug(String slug) {
        Game game = gameRepository.findBySlug(slug)
                .orElseThrow(()-> new NoSuchElementException("Game not found: slug=" + slug));
        return entityToDTO(game);
    }

    // 삭제
    @Override
    public void deleteGame(Long gameId) {
        if(!gameRepository.existsById(gameId)){
            throw new NoSuchElementException("Game not found: id=" + gameId);
        }
        gameRepository.deleteById(gameId);
    }
}
