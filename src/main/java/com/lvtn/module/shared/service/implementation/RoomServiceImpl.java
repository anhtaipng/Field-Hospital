package com.lvtn.module.shared.service.implementation;

import com.lvtn.module.shared.common.ApiSharedMesssage;
import com.lvtn.module.shared.dto.FloorDto;
import com.lvtn.module.shared.dto.RoomDto;
import com.lvtn.module.shared.mapper.SickbedMapper;
import com.lvtn.module.shared.model.*;
import com.lvtn.module.shared.repository.BuildingRepository;
import com.lvtn.module.shared.repository.FloorRepository;
import com.lvtn.module.shared.repository.RoomRepository;
import com.lvtn.module.shared.repository.RoomTypeRepository;
import com.lvtn.module.shared.service.FloorService;
import com.lvtn.module.shared.service.RoomService;
import com.lvtn.platform.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoomServiceImpl implements RoomService {
    @Autowired
    FloorRepository floorRepository;
    @Autowired
    BuildingRepository buildingRepository;
    @Autowired
    RoomTypeRepository roomTypeRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    SickbedMapper sickbedMapper;

    @Override
    public int addRoom(RoomDto roomDto) {
        Optional<Building> building = buildingRepository.findById(roomDto.getBuildingName());
        if (building.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.BUILDING_NOT_FOUND);
        }
        Optional<Floor> floor = floorRepository.findById(new FloorPK(roomDto.getFloorNo(),roomDto.getBuildingName()));
        if (floor.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.FLOOR_NOT_FOUND);
        }

        Optional<RoomType> roomType = roomTypeRepository.findById(roomDto.getRoomType());
        if (roomType.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.ROOM_TYPE_NOT_FOUND);
        }

        Optional<Room> room = roomRepository.findById(new RoomPK(roomDto.getRoomNo(),floor.get()));
        if (room.isPresent()) {
            throw new ApiException(ApiSharedMesssage.ROOM_EXISTED);
        }

        return roomRepository.saveRoom(
                roomDto.getRoomNo(), roomDto.getFloorNo(), roomDto.getBuildingName(), roomDto.getRoomType());
    }

    @Override
    public int updateRoom(RoomDto roomDto) {
        Optional<Building> building = buildingRepository.findById(roomDto.getBuildingName());
        if (building.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.BUILDING_NOT_FOUND);
        }
        Optional<Floor> floor = floorRepository.findById(new FloorPK(roomDto.getFloorNo(),roomDto.getBuildingName()));
        if (floor.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.FLOOR_NOT_FOUND);
        }

        Optional<RoomType> roomType = roomTypeRepository.findById(roomDto.getRoomType());
        if (roomType.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.ROOM_TYPE_NOT_FOUND);
        }

        Optional<Room> room = roomRepository.findById(new RoomPK(roomDto.getRoomNo(),floor.get()));
        if (room.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.ROOM_NOT_FOUND);
        }

        return roomRepository.updateRoom(
                roomDto.getRoomNo(), roomDto.getRoomType(), roomDto.getFloorNo(), roomDto.getBuildingName(),
                room.get().getRoomNo(),room.get().getFloor().getFloorNo(),room.get().getFloor().getBuilding().getBuildingName());
    }

    @Override
    public void deleteRoom(RoomDto roomDto) {
        Optional<Building> building = buildingRepository.findById(roomDto.getBuildingName());
        if (building.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.BUILDING_NOT_FOUND);
        }
        Optional<Floor> floor = floorRepository.findById(new FloorPK(roomDto.getFloorNo(),roomDto.getBuildingName()));
        if (floor.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.FLOOR_NOT_FOUND);
        }

        Optional<RoomType> roomType = roomTypeRepository.findById(roomDto.getRoomType());
        if (roomType.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.ROOM_TYPE_NOT_FOUND);
        }

        Optional<Room> room = roomRepository.findById(new RoomPK(roomDto.getRoomNo(),floor.get()));
        if (room.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.ROOM_NOT_FOUND);
        }
        roomRepository.deleteRoom(roomDto.getRoomNo(),roomDto.getFloorNo(),roomDto.getBuildingName());
    }

    @Override
    public List<RoomDto> getRoomList(FloorDto floorDto) {
        Optional<Building> building = buildingRepository.findById(floorDto.getBuildingName());
        if (building.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.BUILDING_NOT_FOUND);
        }
        if (floorRepository.findById(new FloorPK(floorDto.getFloorNo(),floorDto.getBuildingName())).isEmpty()) {
            throw new ApiException(ApiSharedMesssage.FLOOR_NOT_FOUND);
        }

        return roomRepository.findByFloor_FloorNoAndFloor_Building_BuildingName(floorDto.getFloorNo(), floorDto.getBuildingName()).stream()
                .map(x-> sickbedMapper.mapRoomToRoomDto(x)).collect(Collectors.toList());
    }
}
