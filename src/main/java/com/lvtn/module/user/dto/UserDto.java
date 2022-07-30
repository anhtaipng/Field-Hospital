package com.lvtn.module.user.dto;

import com.lvtn.module.shared.enumeration.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String name;
    private Gender gender;
    private String username;
    //Only pass this from client to server
    private String password;
    private String email;
    private String phone;
    private Date birthDate;
    private String avatarURL;
    private String roleName;
    private String fcmToken;
}
