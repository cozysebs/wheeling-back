package com.lys.wheeling.domain;

import com.lys.wheeling.domain.elist.Category;
import com.lys.wheeling.domain.elist.Difficulty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long gameId;

    private String slug;    //게임을 식별하는 문자열

    @Column(nullable = false)
    private String title;

    private String description;

    private String componentKey;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @CreatedDate
    @Column(updatable = false)
    protected LocalDateTime releasedAt;

    @LastModifiedDate
    protected LocalDateTime updatedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @OneToMany(mappedBy = "game", fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<GameSession> gameSession;

    @OneToMany(mappedBy = "game", fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Post> post;
}
