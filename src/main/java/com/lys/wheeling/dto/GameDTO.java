package com.lys.wheeling.dto;

import com.lys.wheeling.domain.elist.Category;
import com.lys.wheeling.domain.elist.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO {

    private Long gameId;
    private String slug;
    private String title;
    private String description;
    private String component;
    private String thumbnailUrl;
    private Category category;
    private Difficulty difficulty;

}
