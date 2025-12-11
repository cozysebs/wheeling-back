package com.lys.wheeling.controller;

import com.lys.wheeling.dto.BookmarkDTO;
import com.lys.wheeling.security.UserPrincipal;
import com.lys.wheeling.service.bookmark.BookmarkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@Log4j2
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    /**
     * [POST] /api/games/{slug}/bookmarks/toggle
     * - 북마크 토글 (있으면 해제, 없으면 등록)
     */
    @PostMapping("/{slug}/bookmarks/toggle")
    public ResponseEntity<BookmarkDTO> toggleBookmark(@PathVariable("slug") String slug,
                                                      Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            // 비로그인 상태에서 북마크 시도 -> 401
            return ResponseEntity.status(401).build();
        }

        Long userId = principal.getUserId();
        log.info("toggleBookmark: slug={}, userId={}", slug, userId);

        BookmarkDTO dto = bookmarkService.toggleBookmarkForGame(slug, userId);
        return ResponseEntity.ok(dto);
    }

    /**
     * [GET] /api/games/{slug}/bookmarks
     * - 게임별 북마크 상태/개수 조회
     * - 비로그인 사용자의 경우 userId = null 로 전달
     */
    @GetMapping("/{slug}/bookmarks")
    public ResponseEntity<BookmarkDTO> getBookmarkInfo(@PathVariable("slug") String slug,
                                                       Authentication authentication) {
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal principal) {
            userId = principal.getUserId();
        }

        log.info("getBookmarkInfo: slug={}, userId={}", slug, userId);

        BookmarkDTO dto = bookmarkService.getBookmarkInfoForGame(slug, userId);
        return ResponseEntity.ok(dto);
    }

    /**
     * [GET] /api/bookmarks/me
     * - 현재 로그인한 유저의 북마크 게임 리스트
     * - 프로필 페이지에서 사용
     */
    @GetMapping("/bookmarks/me")
    public ResponseEntity<List<BookmarkDTO>> getMyBookmarks(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            return ResponseEntity.status(401).build();
        }

        Long userId = principal.getUserId();
        log.info("getMyBookmarks: userId={}", userId);

        List<BookmarkDTO> list = bookmarkService.getBookmarkedGamesForUser(userId);
        return ResponseEntity.ok(list);
    }
}
