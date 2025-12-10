package com.lys.wheeling.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeDTO {
    
    private Long likeId;    // null일 수 있음 (좋아요 해제 후)
    private Long userId;    // 누가 누른 좋아요인지
    private Long postId;    // 어떤 게시글(=게임)에 대한 좋아요인지
    
    private Long replyId;
    private Long messageId;
    private LocalDateTime createdAt;
    
    private boolean liked;  // 현재 기준으로 좋아요 상태 (true: 눌러져 있음)
    private long likeCount; // 해당 게시글의 전체 좋아요 수
}
