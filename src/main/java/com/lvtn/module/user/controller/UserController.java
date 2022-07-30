package com.lvtn.module.user.controller;

import com.lvtn.module.shared.common.ApiSharedMesssage;
import com.lvtn.module.shared.dto.FcmTokenDto;
import com.lvtn.module.shared.dto.ResetPasswordDto;
import com.lvtn.module.shared.dto.UserSignupDto;
import com.lvtn.module.user.dto.ImportPatientDto;
import com.lvtn.module.user.dto.UserDto;
import com.lvtn.module.user.service.UserService;
import com.lvtn.module.user.service.implementation.SmsService;
import com.lvtn.platform.common.Response;
import com.lvtn.platform.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.text.ParseException;

@RestController
@RequestMapping("/api")
@CrossOrigin
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    SmsService service;


    private final String TOPIC_DESTINATION = "/lesson/sms";

    @GetMapping("/user/profile")
    public Response<UserDto> getUserInfo(Principal user) {
        return Response.success(userService.loadUserProfileByUsername(user.getName()));
    }


    @PostMapping(value = "/user/profile/update")
    public ResponseEntity<?> updateUserProfile(@Valid @RequestBody UserDto userDto,Principal user) {
        UserDto result = userService.updateUserProfile(userDto, user.getName());
        return ResponseEntity.ok(result);
    }

    @PutMapping(value = "/user/fcm_token/update")
    public Response<Void> updateFcmToken(@Valid @RequestBody FcmTokenDto fcmToken, Principal user) {
        try{
            userService.updateFcmToken(fcmToken.getFcmToken(),user.getName());
            return Response.success(ApiSharedMesssage.SUCCESS);
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PutMapping(value = "/user/fcm_token/delete")
    public Response<Void> removeFcmToken(@Valid @RequestBody FcmTokenDto fcmToken, Principal user) {
        try{
            userService.removeFcmToken(fcmToken.getFcmToken(),user.getName());
            return Response.success(ApiSharedMesssage.SUCCESS);
        }
        catch (ApiException apiException){
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
    }

    @PostMapping(value = "/user/send_otp")
    public Response<Void> sendOtp(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
        try{
            service.send(resetPasswordDto);
        }
        catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
        return Response.success(ApiSharedMesssage.SUCCESS);
    }

    @PostMapping(value = "/user/verify_otp")
    public Response<Void> verifyOtp(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
        try{
            userService.resetPassword(resetPasswordDto);
        }
        catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
        return Response.success(ApiSharedMesssage.SUCCESS);
    }

    @PostMapping(value = "/transfer")
    public Response<Void> addImportPatient(@Valid @RequestBody ImportPatientDto importPatientDto) {
        try{
            userService.addImportPatient(importPatientDto);
        }
        catch (ApiException apiException) {
            return Response.error(apiException.getCode(), apiException.getMessage());
        }
        return Response.success(ApiSharedMesssage.SUCCESS);
    }


}
