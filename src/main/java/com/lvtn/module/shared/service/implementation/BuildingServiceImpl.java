package com.lvtn.module.shared.service.implementation;

import com.lvtn.module.shared.common.ApiSharedMesssage;
import com.lvtn.module.shared.dto.BuildingDto;
import com.lvtn.module.shared.mapper.SickbedMapper;
import com.lvtn.module.shared.model.Building;
import com.lvtn.module.shared.repository.BuildingRepository;
import com.lvtn.module.shared.service.BuildingService;
import com.lvtn.platform.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BuildingServiceImpl implements BuildingService {
    @Autowired
    BuildingRepository buildingRepository;
    @Autowired
    SickbedMapper sickbedMapper;

    @Override
    public Building addBuilding(Building building) {
        if (buildingRepository.findById(building.getBuildingName()).isPresent()) {
            throw new ApiException(ApiSharedMesssage.BUILDING_EXISTED);
        }
        return buildingRepository.save(building);
    }

    @Override
    public Building updateBuilding(Building building) {
        if (buildingRepository.findById(building.getBuildingName()).isEmpty()) {
            throw new ApiException(ApiSharedMesssage.BUILDING_NOT_FOUND);
        }
        return buildingRepository.save(building);
    }

    @Override
    public void deleteBuilding(Building building) {
        if (buildingRepository.findById(building.getBuildingName()).isEmpty()) {
            throw new ApiException(ApiSharedMesssage.BUILDING_NOT_FOUND);
        }
        buildingRepository.delete(building);
    }

    @Override
    public List<BuildingDto> getBuildingList() {
        return buildingRepository.findAll().stream().map(x-> sickbedMapper.mapBuildingToBuildingDto(x)).collect(Collectors.toList());
    }
}
