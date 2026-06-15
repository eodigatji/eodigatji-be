package eodigatji.eodigatjiserver.notification.dto;

import java.time.LocalDateTime;

public record NotificationResponseDto(
        Long notificationId,
        Long commentId,
        Long postId,
        String commentContent,
        String commenterNickname,
        Boolean isRead,
        LocalDateTime createdAt
) {}
