package eodigatji.eodigatjiserver.search.dto;

import eodigatji.eodigatjiserver.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchResponseDto {

    private Long postId;
    private Long locationId;

    private String title;
    private String description;

    private String type;
    private String category;

    //Post를 검색 응답 DTO로 변환
    public static SearchResponseDto from (Post post) {
        return SearchResponseDto.builder()
                .postId(post.getId())
                .locationId(post.getLocationId())
                .title(post.getTitle())
                .description(post.getDescription())
                .type(post.getType().name())
                .category(post.getCategory().name())
                .build();
    }
}
