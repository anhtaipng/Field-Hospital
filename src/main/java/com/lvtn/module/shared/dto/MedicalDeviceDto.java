package com.lvtn.module.shared.dto;

import lombok.Data;

@Data
public class MedicalDeviceDto {
    private Long id;
    private String medicalDeviceNumber;
    private String medicalDeviceType;
    private String floorNo;
    private String buildingName;
    private String roomNo;
    private String sickbedNo;
}
