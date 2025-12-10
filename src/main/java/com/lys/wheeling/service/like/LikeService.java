package com.lys.wheeling.service.like;

import com.lys.wheeling.dto.LikeDTO;

public interface LikeService {

    // 게임(slug) 기준으로 좋아요 토글
    LikeDTO toggleLikeForGame(String gameSlug, Long userId);

    // 게임(slug) 기준으로 현재 좋아요 상태/개수 조회
    LikeDTO getLikeInfoForGame(String gameSlug, Long userId);
}
