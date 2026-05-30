package eodigatji.eodigatjiserver.post.dto;

import eodigatji.eodigatjiserver.post.entity.Post;
import eodigatji.eodigatjiserver.post.entity.enums.PostCategory;
import eodigatji.eodigatjiserver.post.entity.enums.PostType;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class PostDetailResponse {
    private Long id;
    private String title;
    private String description;
    private PostType type;
    private PostCategory category;
    private Long locationId;
    private Long userId;
    private LocalDateTime createdAt;

    public static PostDetailResponse from(Post post) {
        return PostDetailResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .type(post.getType())
                .category(post.getCategory())
                .locationId(post.getLocationId())
                .userId(post.getUserId())
                .createdAt(post.getCreatedAt())
                .build();
    }
}