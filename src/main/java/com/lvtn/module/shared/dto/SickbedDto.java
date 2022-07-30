package com.lvtn.module.shared.dto;


import com.lvtn.module.shared.enumeration.SickbedStatus;
import lombok.Data;

import java.util.Date;

@Data
public class SickbedDto {
    private String floorNo;
    private String buildingName;
    private String roomNo;
    private String sickbedNo;
    private SickbedStatus sickbedStatus;
}
