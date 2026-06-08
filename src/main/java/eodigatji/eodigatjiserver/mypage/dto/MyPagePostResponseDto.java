package eodigatji.eodigatjiserver.mypage.dto;

public class MyPagePostResponseDto {

    private final Long postId;
    private final String title;

    public MyPagePostResponseDto(
            Long postId,
            String title
    ) {
        this.postId = postId;
        this.title = title;
    }

    public Long getPostId() {
        return postId;
    }

    public String getTitle() {
        return title;
    }
}