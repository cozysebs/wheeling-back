package com.lys.wheeling.repository;

import com.lys.wheeling.domain.Bookmark;
import com.lys.wheeling.domain.Post;
import com.lys.wheeling.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {

    // 유저 + 게시글 기준 북마크 1개
    Optional<Bookmark> findByUserAndPost(User user, Post post);

    // 게시글 기준 전체 북마크 수
    long countByPost(Post post);

    // 특정 유저가 북마크한 모든 항목
    List<Bookmark> findByUser(User user);

    // 존재 여부만 빠르게 확인
    boolean existsByUserAndPost(User user, Post post);
}
