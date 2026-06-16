package eodigatji.eodigatjiserver.notification.controller;

import eodigatji.eodigatjiserver.auth.annotation.CurrentUser;
import eodigatji.eodigatjiserver.auth.security.AuthenticatedUser;
import eodigatji.eodigatjiserver.notification.dto.NotificationResponseDto;
import eodigatji.eodigatjiserver.notification.service.NotificationService;
import eodigatji.eodigatjiserver.notification.service.SseEmitterManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final SseEmitterManager sseEmitterManager;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@CurrentUser AuthenticatedUser user) {
        return sseEmitterManager.subscribe(user.userId());
    }

    @GetMapping("/comments")
    public List<NotificationResponseDto> getCommentNotifications(@CurrentUser AuthenticatedUser user) {
        return notificationService.getCommentNotifications(user.userId());
    }

    @PatchMapping("/comments/{notificationId}/read")
    public void readNotification(
            @PathVariable Long notificationId,
            @CurrentUser AuthenticatedUser user
    ) {
        notificationService.readNotification(notificationId, user.userId());
    }
}
