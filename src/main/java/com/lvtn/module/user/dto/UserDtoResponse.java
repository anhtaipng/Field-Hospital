package com.lvtn.module.user.dto;

import com.lvtn.module.shared.enumeration.Gender;
import lombok.Data;

import java.util.Date;

@Data
public class UserDtoResponse {
    private String username;
    private String name;
    private String roleName;
    private Gender gender;
    private String phone;
    private Date birthDate;
    private String avatarURL;
    private String email;
}
