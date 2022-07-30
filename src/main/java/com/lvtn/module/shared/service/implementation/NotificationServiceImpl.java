package com.lvtn.module.shared.service.implementation;

import com.lvtn.module.shared.dto.CheckDto;
import com.lvtn.module.shared.dto.NotificationDto;
import com.lvtn.module.shared.dto.ListRequestDto;
import com.lvtn.module.shared.dto.NotificationResponseDto;
import com.lvtn.module.shared.enumeration.NotificationType;
import com.lvtn.module.shared.mapper.CommonMapper;
import com.lvtn.module.shared.model.*;
import com.lvtn.module.shared.repository.NotificationRepository;
import com.lvtn.module.shared.repository.UserRepository;
import com.lvtn.module.shared.service.NotificationService;
import com.lvtn.module.user.dto.PushNotificationRequest;
import com.lvtn.module.user.service.implementation.PushNotificationService;
import com.lvtn.platform.common.PageResponse;
import com.lvtn.platform.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommonMapper commonMapper;

    @Autowired
    PushNotificationService pushNotificationService;

    @Override
    public NotificationDto addNotification(NotificationDto notificationDto, Patient patient, boolean toPatient, boolean toNurse, boolean toDoctor) {
        if (toPatient) {
            notificationRepository.save(commonMapper.mapNotificationDtoToNotification(notificationDto,patient.getUser()));
            if (patient.getUser().getFcmToken()!=null) {
                PushNotificationRequest pushNotificationRequest = new PushNotificationRequest();
                pushNotificationRequest.setTitle(notificationDto.getTitle());
                pushNotificationRequest.setToken(patient.getUser().getFcmToken());
                pushNotificationRequest.setMessage(notificationDto.getDescription());
                pushNotificationService.sendPushNotificationToToken(pushNotificationRequest);
            }
        }
        if (patient.getBvdcGroup()!=null) {
            if (toDoctor) {
                if (patient.getBvdcGroup().getDoctors() != null) {
                    for (Doctor doctor : patient.getBvdcGroup().getDoctors()) {
                        notificationRepository.save(commonMapper.mapNotificationDtoToNotification(notificationDto, doctor.getUser()));
                        if (doctor.getUser().getFcmToken() != null && notificationDto.getNotificationType() != NotificationType.H_SUPPORT) {
                            PushNotificationRequest pushNotificationRequest = new PushNotificationRequest();
                            pushNotificationRequest.setTitle(notificationDto.getTitle());
                            pushNotificationRequest.setToken(doctor.getUser().getFcmToken());
                            pushNotificationRequest.setMessage(notificationDto.getDescription());
                            pushNotificationService.sendPushNotificationToToken(pushNotificationRequest);
                        }
                    }

                }
            }
            if (toNurse) {
                if (patient.getBvdcGroup().getNurses() != null) {
                    for (Nurse nurse : patient.getBvdcGroup().getNurses()) {
                        notificationRepository.save(commonMapper.mapNotificationDtoToNotification(notificationDto, nurse.getUser()));
                        if (nurse.getUser().getFcmToken() != null && notificationDto.getNotificationType() != NotificationType.H_SUPPORT) {
                            PushNotificationRequest pushNotificationRequest = new PushNotificationRequest();
                            pushNotificationRequest.setTitle(notificationDto.getTitle());
                            pushNotificationRequest.setToken(nurse.getUser().getFcmToken());
                            pushNotificationRequest.setMessage(notificationDto.getDescription());
                            pushNotificationService.sendPushNotificationToToken(pushNotificationRequest);
                        }
                    }
                }
            }
        }
        return notificationDto;
    }

    @Override
    public NotificationDto addNotificationWithData(NotificationDto notificationDto, Map<String, String> data, Patient patient, boolean toPatient, boolean toNurse, boolean toDoctor) {
        if (toPatient) {
            notificationRepository.save(commonMapper.mapNotificationDtoToNotification(notificationDto,patient.getUser()));
            if (patient.getUser().getFcmToken()!=null) {
                PushNotificationRequest pushNotificationRequest = new PushNotificationRequest();
                pushNotificationRequest.setTitle(notificationDto.getTitle());
                pushNotificationRequest.setToken(patient.getUser().getFcmToken());
                pushNotificationRequest.setMessage(notificationDto.getDescription());
                pushNotificationService.sendPushNotificationWithDataToToken(pushNotificationRequest,data);
            }
        }
        if (patient.getBvdcGroup()!=null) {
            if (toDoctor) {
                if (patient.getBvdcGroup().getDoctors() != null) {
                    for (Doctor doctor : patient.getBvdcGroup().getDoctors()) {
                        notificationRepository.save(commonMapper.mapNotificationDtoToNotification(notificationDto, doctor.getUser()));
                        if (doctor.getUser().getFcmToken() != null && notificationDto.getNotificationType() != NotificationType.H_SUPPORT) {
                            PushNotificationRequest pushNotificationRequest = new PushNotificationRequest();
                            pushNotificationRequest.setTitle(notificationDto.getTitle());
                            pushNotificationRequest.setToken(doctor.getUser().getFcmToken());
                            pushNotificationRequest.setMessage(notificationDto.getDescription());
                            pushNotificationService.sendPushNotificationWithDataToToken(pushNotificationRequest,data);
                        }
                    }

                }
            }
            if (toNurse) {
                if (patient.getBvdcGroup().getNurses() != null) {
                    for (Nurse nurse : patient.getBvdcGroup().getNurses()) {
                        notificationRepository.save(commonMapper.mapNotificationDtoToNotification(notificationDto, nurse.getUser()));
                        if (nurse.getUser().getFcmToken() != null && notificationDto.getNotificationType() != NotificationType.H_SUPPORT) {
                            PushNotificationRequest pushNotificationRequest = new PushNotificationRequest();
                            pushNotificationRequest.setTitle(notificationDto.getTitle());
                            pushNotificationRequest.setToken(nurse.getUser().getFcmToken());
                            pushNotificationRequest.setMessage(notificationDto.getDescription());
                            pushNotificationService.sendPushNotificationWithDataToToken(pushNotificationRequest,data);
                        }
                    }
                }
            }
        }
        return notificationDto;
    }

    @Override
    public NotificationResponseDto getNotification(ListRequestDto listRequestDto, String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new ApiException(1234321,"Không tìm thấy người dùng");
        }
        Page<Notification> notifications;
        Pageable pageable = PageRequest.of(listRequestDto.getPageNum(), listRequestDto.getPageSize());

        notifications = notificationRepository.findByUser_UsernameOrderByCreateTimeDesc(username,pageable);
        long notRead = notificationRepository.countByUser_UsernameAndStatusFalse(username);
        NotificationResponseDto notificationResponseDto = new NotificationResponseDto();
        notificationResponseDto.setNotRead(notRead);
        notificationResponseDto.setNotificationDtoList(PageResponse.buildPageResponse(notifications.map(commonMapper::mapNotificationToNotificationDto)));
        return notificationResponseDto;
    }

    @Override
    public NotificationDto checkNotification(CheckDto checkDto, Long id) {
        if (id!=null) {
            Optional<Notification >notification = notificationRepository.findById(id);
            if (notification.isPresent()) {
                notification.get().setStatus(checkDto.isCheck());
                return commonMapper.mapNotificationToNotificationDto(notificationRepository.save(notification.get()));
            }
        }
        return null;
    }

    @Override
    public void readAll(String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new ApiException(1234321,"Không tìm thấy người dùng");
        }

        List<Notification> notifications = notificationRepository.findByUser_UsernameAndStatusFalse(username);
        notifications = notifications.stream().peek((notification -> notification.setStatus(true))).collect(Collectors.toList());
        notificationRepository.saveAll(notifications);
    }
}
