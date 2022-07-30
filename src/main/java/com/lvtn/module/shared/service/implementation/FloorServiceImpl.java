package com.lvtn.module.shared.service.implementation;

import com.lvtn.module.shared.common.ApiSharedMesssage;
import com.lvtn.module.shared.dto.FloorDto;
import com.lvtn.module.shared.mapper.SickbedMapper;
import com.lvtn.module.shared.model.Building;
import com.lvtn.module.shared.model.Floor;
import com.lvtn.module.shared.model.FloorPK;
import com.lvtn.module.shared.repository.BuildingRepository;
import com.lvtn.module.shared.repository.FloorRepository;
import com.lvtn.module.shared.service.BuildingService;
import com.lvtn.module.shared.service.FloorService;
import com.lvtn.platform.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FloorServiceImpl implements FloorService {
    @Autowired
    FloorRepository floorRepository;
    @Autowired
    BuildingRepository buildingRepository;
    @Autowired
    SickbedMapper sickbedMapper;

    @Override
    public FloorDto addFloor(FloorDto floorDto) {
        Optional<Building> building = buildingRepository.findById(floorDto.getBuildingName());
        if (building.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.BUILDING_NOT_FOUND);
        }
        if (floorRepository.findById(new FloorPK(floorDto.getFloorNo(),floorDto.getBuildingName())).isPresent()) {
            throw new ApiException(ApiSharedMesssage.FLOOR_EXISTED);
        }
        return sickbedMapper.mapFloorToFloorDto(floorRepository.save(Floor.builder().floorNo(floorDto.getFloorNo()).building(building.get()).build()));
    }

    @Override
    public FloorDto updateFloor(FloorDto floorDto) {
        Optional<Building> building = buildingRepository.findById(floorDto.getBuildingName());
        if (building.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.BUILDING_NOT_FOUND);
        }
        if (floorRepository.findById(new FloorPK(floorDto.getFloorNo(),floorDto.getBuildingName())).isEmpty()) {
            throw new ApiException(ApiSharedMesssage.FLOOR_NOT_FOUND);
        }
        return sickbedMapper.mapFloorToFloorDto(floorRepository.save(sickbedMapper.mapFloorDtoToFloor(floorDto,building.get())));
    }

    @Override
    public void deleteFloor(FloorDto floorDto) {
        Optional<Building> building = buildingRepository.findById(floorDto.getBuildingName());
        if (building.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.BUILDING_NOT_FOUND);
        }
        if (floorRepository.findById(new FloorPK(floorDto.getFloorNo(),floorDto.getBuildingName())).isEmpty()) {
            throw new ApiException(ApiSharedMesssage.FLOOR_NOT_FOUND);
        }
        floorRepository.delete(sickbedMapper.mapFloorDtoToFloor(floorDto,building.get()));
    }

    @Override
    public List<FloorDto> getFloorList(String buildingName) {
        Optional<Building> building = buildingRepository.findById(buildingName);
        if (building.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.BUILDING_NOT_FOUND);
        }
        return floorRepository.findByBuilding_BuildingName(buildingName).stream().map(x -> sickbedMapper.mapFloorToFloorDto(x)).collect(Collectors.toList());
    }
}
