package com.lvtn.module.user.service.implementation;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

import com.lvtn.module.patient.dto.PatientSignupDto;
import com.lvtn.module.shared.dto.ResetPasswordDto;
import com.lvtn.module.shared.dto.UserSignupDto;
import com.lvtn.module.shared.model.Otp;
import com.lvtn.module.shared.model.User;
import com.lvtn.module.shared.repository.OtpRepository;
import com.lvtn.module.shared.repository.UserRepository;
import com.lvtn.module.user.common.ApiClientMessage;
import com.lvtn.platform.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
@Component
public class SmsService {
    @Value("${twilio.account_sid}")
    private String ACCOUNT_SID;
    @Value("${twilio.auth_token}")
    private String AUTH_TOKEN;
    @Value("${twilio.from_number}")
    private String FROM_NUMBER;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void send(ResetPasswordDto resetPasswordDto)  {
        User existed = userRepository.findUserByUsername(resetPasswordDto.getCmnd());
        if (existed == null) {
            throw new ApiException(ApiClientMessage.ACCOUNT_NOT_FOUND);
        };
        if (!existed.getPhone().equals(resetPasswordDto.getPhoneNumber())) {
            throw new ApiException(ApiClientMessage.WRONG_PHONE_NUMBER);
        }

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);


        int min = 1000;
        int max = 9999;
        int number=(int)(Math.random()*(max-min+1)+min);

        existed.setOtpCode(passwordEncoder.encode(String.valueOf(number)));
        existed.setOtpExpireTime(new Date(new Date().getTime() + 90 * 1000));
        userRepository.save(existed);
        String msg ="Mã OTP của bạn là: "+number+ ". Không được chia sẽ mã này với bất kỳ ai";
        try {
            Message message = Message.creator(new PhoneNumber("+84"+resetPasswordDto.getPhoneNumber().substring(1)), new PhoneNumber(FROM_NUMBER), msg)
                    .create();
        }
        catch (Exception e) {
            if (e.getMessage().contains("not a valid")){
                throw new ApiException(1, "Số điện thoại không hợp lệ");
            } else {
                throw new ApiException(1, e.getMessage());
            }
        }


    }

    public void send(PatientSignupDto patientSignupDto)  {
        User user = userRepository.findUserByUsername(patientSignupDto.getCmnd());
        if (user != null) {
            throw new ApiException(ApiClientMessage.ACCOUNT_EXISTED);
        }
        Otp newOtp;
        Optional<Otp> otp = otpRepository.findById(patientSignupDto.getCmnd());
        if (otp.isEmpty()) {
            newOtp = new Otp();
            newOtp.setCmnd(patientSignupDto.getCmnd());
        } else {
            newOtp = otp.get();
        }
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        int min = 1000;
        int max = 9999;
        int number=(int)(Math.random()*(max-min+1)+min);

        newOtp.setOtpCode(passwordEncoder.encode(String.valueOf(number)));
        newOtp.setOtpExpireTime(new Date(new Date().getTime() + 90 * 1000));
        otpRepository.save(newOtp);
        String msg ="Mã OTP của bạn là: "+number+ ". Không được chia sẽ mã này với bất kỳ ai";
        try {
            Message message = Message.creator(new PhoneNumber("+84"+patientSignupDto.getPhone().substring(1)), new PhoneNumber(FROM_NUMBER), msg)
                    .create();
        }
        catch (Exception e) {
            if (e.getMessage().contains("not a valid")){
                throw new ApiException(1, "Số điện thoại không hợp lệ");
            } else {
                throw new ApiException(1, e.getMessage());
            }
        }
    }


}