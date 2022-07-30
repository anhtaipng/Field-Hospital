package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.Examination;
import com.lvtn.module.shared.model.Notification;
import com.lvtn.module.shared.model.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByUser_UsernameOrderByCreateTimeDesc(String username,Pageable pageable);
    List<Notification> findByUser_UsernameAndStatusFalse(String username);
    long countByUser_UsernameAndStatusFalse(String username);
}
