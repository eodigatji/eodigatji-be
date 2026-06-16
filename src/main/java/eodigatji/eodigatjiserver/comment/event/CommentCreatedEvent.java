package eodigatji.eodigatjiserver.comment.event;

public record CommentCreatedEvent(
        Long postId,
        Long commentId,
        String commentContent,
        Long commenterUserId,
        String commenterNickname
) {}
