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
public class MessageDTO {
    private Long messageId;
    private Long senderId;
    private Long receiverId;
    private String content;
    private Long postId;
    private LocalDateTime sentAt;
}
