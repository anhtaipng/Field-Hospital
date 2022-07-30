package com.lvtn.module.shared.dto;


import com.lvtn.module.shared.enumeration.SickbedStatus;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PatientLocationDto {
    private Long id;
    private Date startTime;
    private Date endTime;
    private String floorNo;
    private String buildingName;
    private String roomNo;
    private String roomType;
    private String sickbedNo;
    private SickbedStatus sickbedStatus;
    private List<MedicalDeviceDto> medicalDeviceDtoList;
}
