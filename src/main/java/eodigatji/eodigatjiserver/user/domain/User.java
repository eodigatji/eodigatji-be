package eodigatji.eodigatjiserver.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "student_number", nullable = false, length = 20)
    private String studentNumber;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(nullable = false)
    private Integer temperature;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public User(String email, String password, String studentNumber, String nickname, Integer temperature) {
        this.email = email;
        this.password = password;
        this.studentNumber = studentNumber;
        this.nickname = nickname;
        this.temperature = temperature;
    }
}
