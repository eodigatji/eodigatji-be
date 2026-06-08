package eodigatji.eodigatjiserver.post.dto;

import eodigatji.eodigatjiserver.post.entity.enums.PostCategory;
import eodigatji.eodigatjiserver.post.entity.enums.PostType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUpdateRequest {

    private String title;
    private String description;
    private PostCategory category;
    private Long locationId;
    private PostType type;
}