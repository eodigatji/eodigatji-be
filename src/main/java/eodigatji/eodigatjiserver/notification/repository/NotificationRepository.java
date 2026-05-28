package eodigatji.eodigatjiserver.notification.repository;

import eodigatji.eodigatjiserver.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}