package com.lvtn.module.shared.service;

import com.lvtn.module.shared.dto.FloorDto;
import com.lvtn.module.shared.dto.PatientLocationDto;

import java.util.List;

public interface FloorService {
    FloorDto addFloor(FloorDto floorDto);
    FloorDto updateFloor(FloorDto floorDto);
    void deleteFloor(FloorDto floorDto);
    List<FloorDto> getFloorList(String buildingName);
}
