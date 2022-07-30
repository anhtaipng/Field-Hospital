package com.lvtn.module.shared.dto;


import lombok.Data;

@Data
public class ResetPasswordDto {
    private String phoneNumber;
    private String cmnd;
    private String otp;
    private String newPassword;
}
