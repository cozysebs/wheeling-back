package com.lys.wheeling.dto;

import com.lys.wheeling.domain.elist.Category;
import com.lys.wheeling.domain.elist.Gender;
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
public class UserProfileUpdateRequestDTO {
    private LocalDateTime birthDate;
    private Gender gender;
    private List<Category> favoriteCategories; // 순서가 곧 랭킹
    private String bio;
}
