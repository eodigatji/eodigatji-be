package eodigatji.eodigatjiserver.post.dto;

import eodigatji.eodigatjiserver.post.entity.Post;
import eodigatji.eodigatjiserver.post.entity.enums.PostType;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class PostListResponse {
    private Long id;
    private String title;
    private PostType type;
    private LocalDateTime createdAt;

    public static PostListResponse from(Post post) {
        return PostListResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .type(post.getType())
                .createdAt(post.getCreatedAt())
                .build();
    }
}