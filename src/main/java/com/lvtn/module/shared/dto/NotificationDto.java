package com.lvtn.module.shared.dto;

import com.lvtn.module.shared.enumeration.NotificationType;
import com.lvtn.module.shared.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
public class NotificationDto {
    private Long id;

    private Date createTime;

    private String title;

    private String description;

    private String cmnd;

    private boolean status;

    private NotificationType notificationType;

}
