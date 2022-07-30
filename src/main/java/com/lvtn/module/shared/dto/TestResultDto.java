package com.lvtn.module.shared.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TestResultDto {
    private Long id;
    private Date timeCreated;
    private String testType;
    private Boolean result;
    private Float value;
    private String patientName;
    private String patientCmnd;
}
