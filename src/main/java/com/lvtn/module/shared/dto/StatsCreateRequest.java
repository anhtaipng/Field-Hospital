package com.lvtn.module.shared.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@Builder
public class StatsCreateRequest {
    private Date timeCreated;
    private String statisticType;
    private Float value;
    private Float thuValue;
}
