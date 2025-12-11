package com.lys.wheeling.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkDTO {

    // 기본 식별 정보
    private Long bookmarkId;
    private Long postId;
    private Long userId;
    private LocalDateTime createdAt;

    // 게임 정보 (프로필 북마크 리스트용)
    private Long gameId;
    private String gameSlug;
    private String gameTitle;

    // 현재 게임의 북마크 상태/개수 (토글 API & 조회 API용)
    private boolean bookmarked;
    private long bookmarkCount;
}
