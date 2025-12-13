package com.lys.wheeling.dto;

import com.lys.wheeling.domain.elist.Category;
import com.lys.wheeling.domain.elist.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

// 프로필 조회(About me 모달에서 사용)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileViewDTO {
    private Long userId;
    private String username;
    private String bio;

    // 대표 카테고리 1개
    private Category category;

    // 다중 카테고리(우선순위 포함)
    private List<Category> favoriteCategories;

    private Gender gender;
    private LocalDateTime birthDate;

    // viewer가 본인인지(프론트에서 edit 버튼 노출 판단)
    private boolean owner;

//    private Long userId;
//    private String username;
//    private String password;
//    private String name;
//    private String tel;
//    private String bio;
//    private int age;
//    private LocalDateTime birthDate;
//    private Category category;
//    private Role role;
//    private Gender gender;
//    private LocalDateTime createdAt;
}

