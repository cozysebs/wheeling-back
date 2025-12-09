package com.lys.wheeling.repository;

import com.lys.wheeling.domain.Game;
import com.lys.wheeling.domain.elist.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    // 개별 게임 상세 조회용
    Optional<Game> findBySlug(String slug);

    // gamesConfig를 sync할 때 신규/업데이트 판단용
    boolean existsBySlug(String slug);

    // 후에 카테고리별 게임 목록 기능에 바로 사용 가능
    List<Game> findByCategory(Category category);
}
