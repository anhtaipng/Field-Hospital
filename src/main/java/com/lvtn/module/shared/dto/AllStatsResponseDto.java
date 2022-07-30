package com.lvtn.module.shared.dto;

import com.lvtn.module.shared.enumeration.GroupType;
import com.lvtn.module.shared.model.StatisticType;
import lombok.Data;

import java.util.Date;

@Data
public class AllStatsResponseDto {
    private Long id;
    private Date timeCreated;
    private StatisticType statisticType;
    private StatisticType truongType;
    private String patientCmnd;
    private Float value;
    private Float thuValue;
    private GroupType groupType;
}
