package com.lvtn.module.shared.service;

import com.lvtn.module.shared.dto.CheckDto;
import com.lvtn.module.shared.dto.NotificationDto;
import com.lvtn.module.shared.dto.ListRequestDto;
import com.lvtn.module.shared.dto.NotificationResponseDto;
import com.lvtn.module.shared.model.Patient;
import com.lvtn.platform.common.PageResponse;

import java.util.Map;

public interface NotificationService {
    NotificationDto addNotification(NotificationDto notificationDto, Patient patient, boolean toPatient, boolean toNurse, boolean toDoctor);
    NotificationDto addNotificationWithData(NotificationDto notificationDto, Map<String, String> data, Patient patient, boolean toPatient, boolean toNurse, boolean toDoctor);
    NotificationResponseDto getNotification(ListRequestDto listRequestDto, String username);
    NotificationDto checkNotification(CheckDto checkDto, Long id);
    void readAll(String username);
}
