package eodigatji.eodigatjiserver.mypage.dto;

public class MyPageCommentResponseDto {

    private final Long commentId;
    private final String content;

    public MyPageCommentResponseDto(
            Long commentId,
            String content
    ) {
        this.commentId = commentId;
        this.content = content;
    }

    public Long getCommentId() {
        return commentId;
    }

    public String getContent() {
        return content;
    }
}