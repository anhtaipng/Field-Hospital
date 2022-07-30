package com.lvtn.module.shared.dto;

import com.lvtn.module.shared.enumeration.Gender;
import lombok.Data;

import java.util.Date;

@Data
public class EmployeeDto {
    private String name;
    private Gender gender;
    private String cmnd;
    private String email;
    private String phone;
    private Date birthDate;
    private String avatarURL;
    private String shift;
    private String roleName;
}
