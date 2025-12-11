package com.lys.wheeling.service.bookmark;

import com.lys.wheeling.dto.BookmarkDTO;

import java.util.List;

public interface BookmarkService {

    // 게임 기준 북마크 토글(있으면 해제, 없으면 등록)
    BookmarkDTO toggleBookmarkForGame(String gameSlug, Long userId);

    // 게임 기준 현재 북마크 상태/개수 조회
    BookmarkDTO getBookmarkInfoForGame(String gameSlug, Long userId);

    // 특정 유저가 북마크한 게임 리스트
    List<BookmarkDTO> getBookmarkedGamesForUser(Long userId);
}
