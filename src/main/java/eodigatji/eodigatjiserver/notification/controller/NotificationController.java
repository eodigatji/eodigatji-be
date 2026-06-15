package eodigatji.eodigatjiserver.notification.controller;

import eodigatji.eodigatjiserver.auth.security.AuthenticatedUser;
import eodigatji.eodigatjiserver.notification.dto.NotificationResponseDto;
import eodigatji.eodigatjiserver.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/comments")
    public List<NotificationResponseDto> getCommentNotifications(
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        return notificationService.getCommentNotifications(user.userId());
    }

    @PatchMapping("/comments/{notificationId}/read")
    public void readNotification(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        notificationService.readNotification(notificationId, user.userId());
    }
}
