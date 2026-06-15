package eodigatji.eodigatjiserver.comment.service;

import eodigatji.eodigatjiserver.comment.dto.CommentRequestDto;
import eodigatji.eodigatjiserver.comment.dto.CommentResponseDto;
import eodigatji.eodigatjiserver.comment.entity.Comment;
import eodigatji.eodigatjiserver.comment.repository.CommentRepository;
import java.util.List;

import eodigatji.eodigatjiserver.notification.dto.NotificationResponseDto;
import eodigatji.eodigatjiserver.notification.entity.Notification;
import eodigatji.eodigatjiserver.notification.repository.NotificationRepository;
import eodigatji.eodigatjiserver.notification.service.SseEmitterManager;
import eodigatji.eodigatjiserver.post.entity.Post;
import eodigatji.eodigatjiserver.post.repository.PostRepository;
import eodigatji.eodigatjiserver.user.domain.User;
import eodigatji.eodigatjiserver.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final NotificationRepository notificationRepository;
    private final SseEmitterManager sseEmitterManager;

    public List<CommentResponseDto> getComments(Long postId) {

        List<Comment> comments = commentRepository.findAllByPostId(postId);

        return comments.stream()
                .map(comment -> new CommentResponseDto(
                        comment.getId(),
                        comment.getUser().getId(),
                        comment.getContent(),
                        comment.getCreatedAt()
                ))
                .toList();
    }

    @Transactional
    public void createComment(Long postId, CommentRequestDto request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Comment comment = Comment.builder()
                .user(user)
                .postId(postId) // Post 엔티티 생성 전 임시 처리
                .content(request.content())
                .build();

        commentRepository.save(comment);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if (!post.getUserId().equals(request.userId())) {
            Notification notification = new Notification(post.getUserId(), comment.getId());
            notificationRepository.save(notification);

            sseEmitterManager.send(post.getUserId(), new NotificationResponseDto(
                    notification.getId(),
                    comment.getId(),
                    postId,
                    comment.getContent(),
                    user.getNickname(),
                    notification.getIsRead(),
                    notification.getCreatedAt()
            ));
        }
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (!comment.getPostId().equals(postId)) {
            throw new IllegalArgumentException("해당 게시글의 댓글이 아닙니다.");
        }

        commentRepository.delete(comment);
    }
}