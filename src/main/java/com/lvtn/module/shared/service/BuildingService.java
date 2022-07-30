package com.lvtn.module.shared.service;

import com.lvtn.module.shared.dto.BuildingDto;
import com.lvtn.module.shared.dto.PatientLocationDto;
import com.lvtn.module.shared.model.Building;

import java.util.List;

public interface BuildingService {
    List<BuildingDto> getBuildingList();
    Building addBuilding(Building building);
    Building updateBuilding(Building building);
    void deleteBuilding(Building building);
}
