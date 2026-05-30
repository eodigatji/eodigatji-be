package eodigatji.eodigatjiserver.post.dto;

import eodigatji.eodigatjiserver.post.entity.enums.PostCategory;
import eodigatji.eodigatjiserver.post.entity.enums.PostType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostCreateRequest {
    private String title;
    private String description;
    private PostType type;
    private PostCategory category;
    private Long locationId;

    private List<String> imageUrls;
}