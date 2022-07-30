package com.lvtn.module.shared.model;

import com.lvtn.module.shared.enumeration.NotificationType;
import com.lvtn.module.shared.enumeration.RequirementStatus;
import com.lvtn.module.shared.enumeration.RequirementType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id",nullable = false)
    private User user;

    @Column(nullable = false)
    private boolean status;

    @Column(nullable = false)
    private NotificationType notificationType;

}
