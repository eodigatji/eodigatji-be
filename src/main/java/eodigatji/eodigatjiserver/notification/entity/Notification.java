package eodigatji.eodigatjiserver.notification.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: User 엔티티 생성 후 연관관계 매핑 예정
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    @Column(nullable = false)
    private Boolean isRead = false;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Notification(Long userId, Long commentId) {
        this.userId = userId;
        this.commentId = commentId;
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
    }

    public void read() {
        this.isRead = true;
    }
}