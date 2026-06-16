package eodigatji.eodigatjiserver.notification.listener;

import eodigatji.eodigatjiserver.comment.event.CommentCreatedEvent;
import eodigatji.eodigatjiserver.notification.dto.NotificationResponseDto;
import eodigatji.eodigatjiserver.notification.entity.Notification;
import eodigatji.eodigatjiserver.notification.repository.NotificationRepository;
import eodigatji.eodigatjiserver.notification.service.SseEmitterManager;
import eodigatji.eodigatjiserver.post.entity.Post;
import eodigatji.eodigatjiserver.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final PostRepository postRepository;
    private final NotificationRepository notificationRepository;
    private final SseEmitterManager sseEmitterManager;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(CommentCreatedEvent event) {
        Post post = postRepository.findById(event.postId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if (post.getUserId().equals(event.commenterUserId())) return;

        Notification notification = new Notification(post.getUserId(), event.commentId());
        notificationRepository.save(notification);

        sseEmitterManager.send(post.getUserId(), new NotificationResponseDto(
                notification.getId(),
                event.commentId(),
                event.postId(),
                event.commentContent(),
                event.commenterNickname(),
                notification.getIsRead(),
                notification.getCreatedAt()
        ));
    }
}
