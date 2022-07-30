package com.lvtn.module.shared.dto;

import lombok.Data;

@Data
public class BuildingDto {
    private String buildingName;
    private int emptySickbed;
    private int usedSickbed;
    private int requestedSickbed;
    private int disableSickbed;
}
