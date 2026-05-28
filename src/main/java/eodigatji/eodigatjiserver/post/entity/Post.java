package eodigatji.eodigatjiserver.post.entity;

import eodigatji.eodigatjiserver.post.entity.enums.PostCategory;
import eodigatji.eodigatjiserver.post.entity.enums.PostType;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: Location 엔티티 작업 완료 시 연관관계 매핑 (@ManyToOne)으로 변경
    @Column(name = "location_id", nullable = false)
    private Long locationId;

    // TODO: User 엔티티 작업 완료 시 연관관계 매핑 (@ManyToOne)으로 변경
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PostType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PostCategory category;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Post(Long locationId, Long userId, String title, String description, PostType type, PostCategory category) {
        this.locationId = locationId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.type = type;
        this.category = category;
        this.createdAt = LocalDateTime.now();
    }
}