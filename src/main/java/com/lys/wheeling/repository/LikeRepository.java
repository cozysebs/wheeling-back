package com.lys.wheeling.repository;

import com.lys.wheeling.domain.Like;
import com.lys.wheeling.domain.Post;
import com.lys.wheeling.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like,Long> {
    
    Optional<Like> findByUserAndPost(User user, Post post);
    
    long countByPost(Post post);
    
    // "처음 화면 로딩 시 이 유저가 이미 좋아요를 눌렀는지"를 빠르게 체크함
    boolean existsByUserAndPost(User user, Post post);
}
