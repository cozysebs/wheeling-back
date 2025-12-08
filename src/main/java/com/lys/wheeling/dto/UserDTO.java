package com.lys.wheeling.dto;

import com.lys.wheeling.domain.elist.Category;
import com.lys.wheeling.domain.elist.Gender;
import com.lys.wheeling.domain.elist.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long userId;
    private String username;
    private String password;
    private String name;
    private String tel;
    private String bio;
    private int age;
    private Category category;
    private Role role;
    private Gender gender;
    private LocalDateTime createdAt;
}
