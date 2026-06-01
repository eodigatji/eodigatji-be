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
    private String thumbnailImageUrl;
    private LocalDateTime createdAt;

    public static PostListResponse from(Post post) {
        String thumbnailImageUrl = post.getImageUrls().isEmpty()
                ? null
                : post.getImageUrls().get(0);

        return PostListResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .type(post.getType())
                .thumbnailImageUrl(thumbnailImageUrl)
                .createdAt(post.getCreatedAt())
                .build();
    }
}