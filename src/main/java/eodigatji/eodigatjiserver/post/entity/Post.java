package eodigatji.eodigatjiserver.post.entity;

import eodigatji.eodigatjiserver.post.entity.enums.PostCategory;
import eodigatji.eodigatjiserver.post.entity.enums.PostType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "post_image",
            joinColumns = @JoinColumn(name = "post_id")
    )
    @Column(name = "image_url", nullable = false)
    private List<String> imageUrls = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Post(
            Long locationId,
            Long userId,
            String title,
            String description,
            PostType type,
            PostCategory category,
            List<String> imageUrls
    ) {
        this.locationId = locationId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.type = type;
        this.category = category;
        this.imageUrls = imageUrls == null ? new ArrayList<>() : new ArrayList<>(imageUrls);
        this.createdAt = LocalDateTime.now();
    }

    public void update(
            String title,
            String description,
            PostCategory category,
            Long locationId,
            PostType type,
            List<String> imageUrls
    ) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.locationId = locationId;
        this.type = type;
        this.updatedAt = LocalDateTime.now();

        if (imageUrls != null) {
            this.imageUrls.clear();
            this.imageUrls.addAll(imageUrls);
        }
    }
}