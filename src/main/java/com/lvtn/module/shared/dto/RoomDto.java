package com.lvtn.module.shared.dto;


import lombok.Data;

import java.util.Date;

@Data
public class RoomDto {
    private String floorNo;
    private String buildingName;
    private String roomNo;
    private String roomType;
    private int emptySickbed;
    private int usedSickbed;
    private int requestedSickbed;
    private int disableSickbed;
}
