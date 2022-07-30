package com.lvtn.module.user.dto;

import com.lvtn.module.shared.enumeration.Gender;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
public class ImportPatientDto {
    private Long id;
    private String cmnd, name, phone, email, tinh, huyen, xa, thon, bhyt;
    private Gender gender;
    private Date birthDay;
    private ArrayList<String> sickness;
}
