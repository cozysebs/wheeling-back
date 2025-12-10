package com.lys.wheeling.controller;

import com.lys.wheeling.dto.LikeDTO;
import com.lys.wheeling.security.CustomUserDetailService;
import com.lys.wheeling.security.UserPrincipal;
import com.lys.wheeling.service.like.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
@Log4j2
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikeController {

    private final LikeService likeService;

    // [POST] /api/games/{slug}/likes/toggle
    // 좋아요 토글 (있으면 취소, 없으면 생성)
    @PostMapping("/{slug}/likes/toggle")
    public ResponseEntity<LikeDTO> toggleLike(@PathVariable("slug") String slug,
                                              Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Long userId = principal.getUserId();

        LikeDTO dto = likeService.toggleLikeForGame(slug, userId);
        return ResponseEntity.ok(dto);
    }
    
    // [GET] /api/games/{slug}/likes
    // 현재 좋아요 상태/개수 조회. 비로그인 사용자의 경우 Authentication이 null일 수 있음
    @GetMapping("/{slug}/likes")
    public ResponseEntity<LikeDTO> getLikeInfo(@PathVariable("slug") String slug,
                                               Authentication authentication) {
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal principal){
            userId = principal.getUserId();
        }

        LikeDTO dto = likeService.getLikeInfoForGame(slug, userId);
        return ResponseEntity.ok(dto);
    }
    
}
