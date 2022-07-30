package com.lvtn.module.shared.dto;

import com.lvtn.module.shared.enumeration.GroupType;
import lombok.Data;

import java.util.Date;

@Data
public class StatsResponseDto {
    private Long id;
    private Date timeCreated;
    private String statisticType;
    private String patientCmnd;
    private Float value;
    private Float thuValue;
    private GroupType groupType;
}
