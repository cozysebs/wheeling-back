package com.lys.wheeling.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDTO {
    private Long replyId;
    private String content;
    private Long userId;
    private Long postId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long parentId;
    private List<ReplyDTO> replies;
}
