package eodigatji.eodigatjiserver.comment.dto;

import java.time.LocalDateTime;

public record CommentResponseDto(
        Long commentId,
        Long userId,
        String content,
        LocalDateTime createdAt
) { }