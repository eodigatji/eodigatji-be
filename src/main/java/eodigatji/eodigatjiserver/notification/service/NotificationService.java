package eodigatji.eodigatjiserver.notification.service;

import eodigatji.eodigatjiserver.comment.entity.Comment;
import eodigatji.eodigatjiserver.comment.repository.CommentRepository;
import eodigatji.eodigatjiserver.notification.dto.NotificationResponseDto;
import eodigatji.eodigatjiserver.notification.entity.Notification;
import eodigatji.eodigatjiserver.notification.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final CommentRepository commentRepository;

    public List<NotificationResponseDto> getCommentNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findAllByUserIdOrderByCreatedAtDesc(userId);

        return notifications.stream()
                .map(notification -> {
                    Comment comment = commentRepository.findById(notification.getCommentId())
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
                    return new NotificationResponseDto(
                            notification.getId(),
                            comment.getId(),
                            comment.getPostId(),
                            comment.getContent(),
                            comment.getUser().getNickname(),
                            notification.getIsRead(),
                            notification.getCreatedAt()
                    );
                })
                .toList();
    }

    @Transactional
    public void readNotification(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 알림입니다."));

        if (!notification.getUserId().equals(userId)) {
            throw new IllegalArgumentException("해당 알림에 대한 권한이 없습니다.");
        }

        notification.read();
    }
}
