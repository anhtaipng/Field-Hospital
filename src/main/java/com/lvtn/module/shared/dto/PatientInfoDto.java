package com.lvtn.module.shared.dto;


import com.lvtn.module.shared.enumeration.Gender;
import com.lvtn.module.shared.enumeration.GroupType;
import com.lvtn.module.shared.enumeration.PatientStatusType;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
public class PatientInfoDto {
    private String name;
    private Gender gender;
    private Date birthDate;
    private String bhyt;
    private String tinh;
    private String huyen;
    private String xa;
    private String thon;
    private String cmnd;
    private GroupType groupType;
    private PatientStatusType status;
    private String avatarURL;
    private String email;
    @Pattern(regexp = "^[0-9]*$",message = "Điện thoại chỉ bao gồm các ký tự số")
    private String phone;
    private Date hospitalizedDate;
    private Date dischargeDate;
    private Boolean isAutoGroup;
}
