package com.fixmate.service;

import com.fixmate.dto.NotificationDTO;
import com.fixmate.model.Notification;
import com.fixmate.model.User;
import com.fixmate.repository.NotificationRepository;
import com.fixmate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public List<NotificationDTO> getAllNotifications() {
        return notificationRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<NotificationDTO> getNotificationsByUser(Long userId) {
        return notificationRepository.findByUserId(userId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<NotificationDTO> getUnreadByUser(Long userId) {
        return notificationRepository.findByUserIdAndIsRead(userId, false).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public NotificationDTO createNotification(NotificationDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Notification notification = Notification.builder()
                .user(user)
                .message(dto.getMessage())
                .isRead(false)
                .build();
        return toDTO(notificationRepository.save(notification));
    }

    public NotificationDTO markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        notification.setRead(true);
        return toDTO(notificationRepository.save(notification));
    }

    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    private NotificationDTO toDTO(Notification n) {
        return NotificationDTO.builder()
                .id(n.getId())
                .userId(n.getUser().getId())
                .message(n.getMessage())
                .isRead(n.isRead())
                .build();
    }
}
