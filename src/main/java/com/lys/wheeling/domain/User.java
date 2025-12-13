package com.lys.wheeling.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lys.wheeling.domain.elist.Category;
import com.lys.wheeling.domain.elist.Gender;
import com.lys.wheeling.domain.elist.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(exclude = { "gameSession", "bookmark", "like", "reply"})
//@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
//    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private String name;

    private String bio;

    private int age;

    @Column(nullable = false, unique = true)
    private String tel;

    @Column(name = "birth_date")
    private LocalDateTime birthDate;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Transient
//    @JsonIgnore
    private String token;

    @CreatedDate
    @Column(updatable = false)
    protected LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    protected LocalDateTime updatedAt;

    // 열거형
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    // ✅ 다중 선호 카테고리(우선순위 유지)
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "user_favorite_categories",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @OrderColumn(name = "preference_rank") // 0이 1순위
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private List<Category> favoriteCategories = new ArrayList<>();

    //OneToMany
    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<GameSession> gameSession;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Bookmark> bookmark;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Like> like;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Reply> reply;


}
