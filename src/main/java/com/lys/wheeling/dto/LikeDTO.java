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
    private Long likeId;
    private Long userId;
    private Long postId;
    private Long replyId;
    private Long messageId;
    private LocalDateTime createdAt;
}
