package com.lys.wheeling.repository;

import com.lys.wheeling.domain.Game;
import com.lys.wheeling.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    // Game 기준으로 Post 찾기 (1:1 관계)
    Optional<Post> findByGame(Game game);
}
