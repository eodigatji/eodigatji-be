package eodigatji.eodigatjiserver.search.dto;

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
}
