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
public class GameSessionDTO {
    private Long sessionId;
    private Long gameId;
    private Long userId;
    private int score;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

}
