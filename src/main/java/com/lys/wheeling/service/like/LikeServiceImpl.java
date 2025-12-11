package com.lys.wheeling.service.like;

import com.lys.wheeling.domain.Game;
import com.lys.wheeling.domain.Like;
import com.lys.wheeling.domain.Post;
import com.lys.wheeling.domain.User;
import com.lys.wheeling.dto.LikeDTO;
import com.lys.wheeling.repository.GameRepository;
import com.lys.wheeling.repository.LikeRepository;
import com.lys.wheeling.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    // 게임(slug) 기준으로 좋아요 토글. 없다면 생성, 이미 있다면 삭제
    @Override
    public LikeDTO toggleLikeForGame(String gameSlug, Long userId) {

        // 1) Game 조회
        Game game = gameRepository.findBySlug(gameSlug)
                .orElseThrow(() -> new EntityNotFoundException("Game not found: " + gameSlug));

        // 2) Game:Post = 1:1 전제
        Post post = game.getPost();
        if(post == null) {
            throw new IllegalStateException("Post not found: " + gameSlug);
        }

        // 3) User 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        // 4) 해당 유저가 이 게시글에 이미 좋아요를 눌렀는지 확인
        Like existing = likeRepository.findByUserAndPost(user, post).orElse(null);

        boolean liked;
        Long likeId = null;

        if (existing != null) {
            // 이미 좋아요가 있으면 삭제하기
            likeRepository.delete(existing);
            liked = false;
        }else {
            // 아직 좋아요가 없으면 새로 생성하기
            Like newLike = Like.builder()
                    .user(user)
                    .post(post)
                    .build();
            Like saved = likeRepository.save(newLike);
            liked = true;
            likeId = saved.getLikeId();
        }

        // 5) 현재 게시글의 전체 좋아요 수 재계산
        long likeCount = likeRepository.countByPost(post);

        return LikeDTO.builder()
                .likeId(likeId)
                .postId(post.getPostId())
                .userId(user.getUserId())
                .liked(liked)
                .likeCount(likeCount)
                .build();
    }

    // 게임(slug) 기준으로 현재 좋아요 상태/개수 조회. userId가 null이면(비로그인) 내 좋아요 상태는 false로 처리하고,
    // 전체 likeCount만 반환하는 식으로 응용 가능.
    @Override
    @Transactional(readOnly = true)
    public LikeDTO getLikeInfoForGame(String gameSlug, Long userId) {
        log.info("LikeServiceImpl: {}, {}", gameSlug,  userId);
        // 1) Game 조회
        Game game = gameRepository.findBySlug(gameSlug)
                .orElseThrow(() -> new EntityNotFoundException("Game not found: " + gameSlug));

        // 2) Post 조회
        Post post = game.getPost();
        if(post == null) {
            throw new IllegalStateException("Post not found: " + gameSlug);
        }

        // 3) 전체 좋아요 개수
        long likeCount = likeRepository.countByPost(post);

        boolean liked = false;
        Long likeId = null;

        // 4) 로그인한 유저인 경우에만 내 좋아요 상태 조회
        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

            Like existing = likeRepository.findByUserAndPost(user, post).orElse(null);

            if(existing != null) {
                liked = true;
                likeId = existing.getLikeId();
            }
        }

        return LikeDTO.builder()
                .likeId(likeId)
                .postId(post.getPostId())
                .userId(userId)
                .liked(liked)
                .likeCount(likeCount)
                .build();
    }
}
