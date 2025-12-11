package com.lys.wheeling.service.bookmark;

import com.lys.wheeling.domain.Bookmark;
import com.lys.wheeling.domain.Game;
import com.lys.wheeling.domain.Post;
import com.lys.wheeling.domain.User;
import com.lys.wheeling.dto.BookmarkDTO;
import com.lys.wheeling.repository.BookmarkRepository;
import com.lys.wheeling.repository.GameRepository;
import com.lys.wheeling.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    // ====== 공통 유틸 ======

    private Game getGameBySlug(String gameSlug) {
        return gameRepository.findBySlug(gameSlug)
                .orElseThrow(() -> new EntityNotFoundException("Game not found: " + gameSlug));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    }

    //toDTO를 왜 이렇게 두 가지 형태로 만들었을까?
    private BookmarkDTO toDTO(Bookmark bookmark, long bookmarkCount, boolean bookmarked) {
        Game game = bookmark.getPost().getGame();
        return BookmarkDTO.builder()
                .bookmarkId(bookmark.getBookmarkId())
                .postId(bookmark.getPost().getPostId())
                .userId(bookmark.getUser().getUserId())
                .createdAt(bookmark.getCreatedAt())
                .gameId(game.getGameId())
                .gameSlug(game.getSlug())
                .gameTitle(game.getTitle())
                .bookmarked(bookmarked)
                .bookmarkCount(bookmarkCount)
                .build();
    }

    private BookmarkDTO toDTO(Game game, User user, Long bookmarkId,
                              long bookmarkCount,  boolean bookmarked) {
        Post post = game.getPost();
        return BookmarkDTO.builder()
                .bookmarkId(bookmarkId)
                .postId(post.getPostId())
                .userId(user != null ? user.getUserId() : null)
                .createdAt(null)
                .gameId(game.getGameId())
                .gameSlug(game.getSlug())
                .gameTitle(game.getTitle())
                .bookmarked(bookmarked)
                .bookmarkCount(bookmarkCount)
                .build();
    }

    // ====== 토글 ======
    @Override
    public BookmarkDTO toggleBookmarkForGame(String gameSlug, Long userId) {
        Game game = getGameBySlug(gameSlug);
        Post post = game.getPost();
        if(post == null) {
            throw new IllegalStateException("Post not found for game: " + gameSlug);
        }

        User user = getUserById(userId);

        Bookmark existing = bookmarkRepository.findByUserAndPost(user, post).orElse(null);

        boolean bookmarked;
        Long bookmarkId = null;

        if(existing != null) {
            bookmarkRepository.delete(existing);
            bookmarked = false;
        } else {
            Bookmark newBookmark = Bookmark.builder()
                    .user(user)
                    .post(post)
                    .build();
            Bookmark saved = bookmarkRepository.save(newBookmark);
            bookmarked = true;
            bookmarkId = saved.getBookmarkId();
        }

        long bookmarkCount = bookmarkRepository.countByPost(post);

        return toDTO(game, user, bookmarkId, bookmarkCount, bookmarked);
    }

    // ====== 게임별 북마크 정보 조회 ======
    @Override
    @Transactional(readOnly = true)
    public BookmarkDTO getBookmarkInfoForGame(String gameSlug, Long userId) {

        Game game = getGameBySlug(gameSlug);
        Post post = game.getPost();
        if(post == null) {
            throw new IllegalStateException("Post not found for game2: " + gameSlug);
        }

        long bookmarkCount = bookmarkRepository.countByPost(post);

        boolean bookmarked = false;
        Long bookmarkId = null;
        User user = null;

        if(userId != null) {
            user = getUserById(userId);
            Bookmark existing = bookmarkRepository.findByUserAndPost(user, post).orElse(null);
            if(existing != null) {
                bookmarked = true;
                bookmarkId = existing.getBookmarkId();
            }
        }

        return toDTO(game, user, bookmarkId, bookmarkCount, bookmarked);
    }

    // ====== 유저별 북마크 게임 리스트 ======
    @Override
    @Transactional(readOnly = true)
    public List<BookmarkDTO> getBookmarkedGamesForUser(Long userId) {

        User user = getUserById(userId);
        List<Bookmark> bookmarks = bookmarkRepository.findByUser(user);

        return bookmarks.stream()
                .map(bookmark -> {
                    Post post = bookmark.getPost();
                    Game game = post.getGame();
                    long count = bookmarkRepository.countByPost(post);
                    return BookmarkDTO.builder()
                            .bookmarkId(bookmark.getBookmarkId())
                            .postId(post.getPostId())
                            .userId(user.getUserId())
                            .createdAt(bookmark.getCreatedAt())
                            .gameId(game.getGameId())
                            .gameSlug(game.getSlug())
                            .gameTitle(game.getTitle())
                            .bookmarked(true)      // 내 리스트이므로 항상 true
                            .bookmarkCount(count)
                            .build();
                })
                .toList();
    }
}
