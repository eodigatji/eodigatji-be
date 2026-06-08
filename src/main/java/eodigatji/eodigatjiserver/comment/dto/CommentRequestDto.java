package eodigatji.eodigatjiserver.comment.dto;

public record CommentRequestDto(
        Long userId,
        String content
) {
}