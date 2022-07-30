package com.lvtn.module.shared.dto;


import lombok.Data;

import java.util.Date;

@Data
public class FloorDto {
    private String floorNo;
    private String buildingName;
    private int emptySickbed;
    private int usedSickbed;
    private int requestedSickbed;
    private int disableSickbed;
}
