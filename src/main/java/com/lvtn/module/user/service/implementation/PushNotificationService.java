package com.lvtn.module.user.service.implementation;

import com.lvtn.module.user.dto.PushNotificationRequest;
import com.lvtn.platform.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PushNotificationService {

    private Logger logger = LoggerFactory.getLogger(PushNotificationService.class);

    private FCMService fcmService;

    public PushNotificationService(FCMService fcmService) {
        this.fcmService = fcmService;
    }


    public void sendPushNotificationToToken(PushNotificationRequest request) {
        try {
            fcmService.sendMessageToToken(request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
//            throw new ApiException(12341234,e.getMessage());
        }
    }

    public void sendPushNotificationWithDataToToken(PushNotificationRequest request, Map<String,String> data) {
        try {
            fcmService.sendMessageWithDataToToken(data, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}