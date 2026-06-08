package eodigatji.eodigatjiserver.location.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@Table(name = "location")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "detail", nullable = false, length = 255)
    private String detail;

    @Column(name = "number", nullable = false, length = 20)
    private String number;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public void updateName(String name) {
        this.name = name;
    }

    public void updateDetail(String detail) {
        this.detail = detail;
    }

    public void updateNumber(String number) {
        this.number = number;
    }

    @PrePersist
    protected void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
