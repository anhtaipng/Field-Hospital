package com.lvtn.module.shared.service.implementation;

import com.lvtn.module.shared.common.ApiSharedMesssage;
import com.lvtn.module.shared.dto.PatientLocationDto;
import com.lvtn.module.shared.dto.RoomDto;
import com.lvtn.module.shared.dto.SickbedDto;
import com.lvtn.module.shared.dto.SickbedListRequestDto;
import com.lvtn.module.shared.enumeration.SickbedStatus;
import com.lvtn.module.shared.mapper.CommonMapper;
import com.lvtn.module.shared.mapper.SickbedMapper;
import com.lvtn.module.shared.model.*;
import com.lvtn.module.shared.repository.*;
import com.lvtn.module.shared.service.RoomService;
import com.lvtn.module.shared.service.SickbedService;
import com.lvtn.platform.common.PageResponse;
import com.lvtn.platform.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SickbedServiceImpl implements SickbedService {
    @Autowired
    FloorRepository floorRepository;
    @Autowired
    BuildingRepository buildingRepository;
    @Autowired
    RoomTypeRepository roomTypeRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    SickbedRepository sickbedRepository;
    @Autowired
    SickbedMapper sickbedMapper;
    @Autowired
    CommonMapper commonMapper;

    @Override
    public int addSickbed(SickbedDto sickbedDto) {
        Optional<Building> building = buildingRepository.findById(sickbedDto.getBuildingName());
        if (building.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.BUILDING_NOT_FOUND);
        }
        Optional<Floor> floor = floorRepository.findById(new FloorPK(sickbedDto.getFloorNo(), sickbedDto.getBuildingName()));
        if (floor.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.FLOOR_NOT_FOUND);
        }

        Optional<Room> room = roomRepository.findById(new RoomPK(sickbedDto.getRoomNo(), floor.get()));
        if (room.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.ROOM_NOT_FOUND);
        }

        Optional<Sickbed> sickbed = sickbedRepository.findById(new SickbedPK(sickbedDto.getSickbedNo(), room.get()));
        if (sickbed.isPresent()) {
            throw new ApiException(ApiSharedMesssage.SICKBED_EXISTED);
        }

        if (sickbedDto.getSickbedStatus() == null) {
            sickbedDto.setSickbedStatus(SickbedStatus.EMPTY);
        }

        return sickbedRepository.saveSickbed(sickbedDto.getSickbedNo(), sickbedDto.getRoomNo(), sickbedDto.getFloorNo(), sickbedDto.getBuildingName(), String.valueOf(sickbedDto.getSickbedStatus()));
    }

    @Override
    public int updateSickbed(SickbedDto sickbedDto) {
        Optional<Building> building = buildingRepository.findById(sickbedDto.getBuildingName());
        if (building.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.BUILDING_NOT_FOUND);
        }
        Optional<Floor> floor = floorRepository.findById(new FloorPK(sickbedDto.getFloorNo(), sickbedDto.getBuildingName()));
        if (floor.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.FLOOR_NOT_FOUND);
        }

        Optional<Room> room = roomRepository.findById(new RoomPK(sickbedDto.getRoomNo(), floor.get()));
        if (room.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.ROOM_NOT_FOUND);
        }

        Optional<Sickbed> sickbed = sickbedRepository.findById(new SickbedPK(sickbedDto.getSickbedNo(), room.get()));
        if (sickbed.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.SICKBED_NOT_FOUND);
        }

        return sickbedRepository.updateSickbed(sickbedDto.getSickbedNo(), sickbedDto.getRoomNo(), sickbedDto.getFloorNo(), sickbedDto.getBuildingName(),
                String.valueOf(sickbedDto.getSickbedStatus()), sickbed.get().getSickBedNo(), sickbed.get().getRoom().getRoomNo(), sickbed.get().getRoom().getFloor().getFloorNo(),
                sickbed.get().getRoom().getFloor().getBuilding().getBuildingName());

    }

    @Override
    public void deleteSickbed(SickbedDto sickbedDto) {
        Optional<Building> building = buildingRepository.findById(sickbedDto.getBuildingName());
        if (building.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.BUILDING_NOT_FOUND);
        }
        Optional<Floor> floor = floorRepository.findById(new FloorPK(sickbedDto.getFloorNo(), sickbedDto.getBuildingName()));
        if (floor.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.FLOOR_NOT_FOUND);
        }

        Optional<Room> room = roomRepository.findById(new RoomPK(sickbedDto.getRoomNo(), floor.get()));
        if (room.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.ROOM_NOT_FOUND);
        }

        Optional<Sickbed> sickbed = sickbedRepository.findById(new SickbedPK(sickbedDto.getSickbedNo(), room.get()));
        if (sickbed.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.SICKBED_NOT_FOUND);
        }
        sickbedRepository.deleteSickbed(sickbedDto.getSickbedNo(), sickbedDto.getRoomNo(), sickbedDto.getFloorNo(), sickbedDto.getBuildingName());
    }

    @Override
    public void enableSickbed(SickbedDto sickbedDto) {
        Optional<Building> building = buildingRepository.findById(sickbedDto.getBuildingName());
        if (building.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.BUILDING_NOT_FOUND);
        }
        Optional<Floor> floor = floorRepository.findById(new FloorPK(sickbedDto.getFloorNo(), sickbedDto.getBuildingName()));
        if (floor.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.FLOOR_NOT_FOUND);
        }

        Optional<Room> room = roomRepository.findById(new RoomPK(sickbedDto.getRoomNo(), floor.get()));
        if (room.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.ROOM_NOT_FOUND);
        }

        Optional<Sickbed> sickbed = sickbedRepository.findById(new SickbedPK(sickbedDto.getSickbedNo(), room.get()));
        if (sickbed.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.SICKBED_NOT_FOUND);
        }
        if (sickbed.get().getSickbedStatus() == SickbedStatus.EMPTY) {
            throw new ApiException((ApiSharedMesssage.SICKBED_EMPTY));
        }
        if (sickbed.get().getSickbedStatus() == SickbedStatus.USED) {
            throw new ApiException((ApiSharedMesssage.SICKBED_USED));
        }
        if (sickbed.get().getSickbedStatus() == SickbedStatus.REQUESTED) {
            throw new ApiException((ApiSharedMesssage.SICKBED_REQUESTED));
        }
        sickbedRepository.updateSickbed(sickbedDto.getSickbedNo(), sickbedDto.getRoomNo(), sickbedDto.getFloorNo(), sickbedDto.getBuildingName(),
                "EMPTY", sickbedDto.getSickbedNo(), sickbedDto.getRoomNo(), sickbedDto.getFloorNo(), sickbedDto.getBuildingName());
    }

    @Override
    public void disableSickbed(SickbedDto sickbedDto) {
        Optional<Building> building = buildingRepository.findById(sickbedDto.getBuildingName());
        if (building.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.BUILDING_NOT_FOUND);
        }
        Optional<Floor> floor = floorRepository.findById(new FloorPK(sickbedDto.getFloorNo(), sickbedDto.getBuildingName()));
        if (floor.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.FLOOR_NOT_FOUND);
        }

        Optional<Room> room = roomRepository.findById(new RoomPK(sickbedDto.getRoomNo(), floor.get()));
        if (room.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.ROOM_NOT_FOUND);
        }

        Optional<Sickbed> sickbed = sickbedRepository.findById(new SickbedPK(sickbedDto.getSickbedNo(), room.get()));
        if (sickbed.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.SICKBED_NOT_FOUND);
        }
        if (sickbed.get().getSickbedStatus() == SickbedStatus.DISABLE) {
            throw new ApiException((ApiSharedMesssage.SICKBED_DISABLE));
        }
        if (sickbed.get().getSickbedStatus() == SickbedStatus.USED) {
            throw new ApiException((ApiSharedMesssage.SICKBED_USED));
        }
        if (sickbed.get().getSickbedStatus() == SickbedStatus.REQUESTED) {
            throw new ApiException((ApiSharedMesssage.SICKBED_REQUESTED));
        }
        sickbedRepository.updateSickbed(sickbedDto.getSickbedNo(), sickbedDto.getRoomNo(), sickbedDto.getFloorNo(), sickbedDto.getBuildingName(),
                "DISABLE", sickbedDto.getSickbedNo(), sickbedDto.getRoomNo(), sickbedDto.getFloorNo(), sickbedDto.getBuildingName());
    }

    @Override
    public PageResponse<PatientLocationDto> getSickbedAvailableWithRoomType(SickbedListRequestDto sickbedListRequestDto) {
        Optional<RoomType> room = roomTypeRepository.findById(sickbedListRequestDto.getRoomType());
        if (room.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.ROOM_TYPE_NOT_FOUND);
        }
        Pageable pageable = PageRequest.of(sickbedListRequestDto.getPageNum(), sickbedListRequestDto.getPageSize());
        Page<Sickbed> sickbeds;
        if (sickbedListRequestDto.getSearch()==null || sickbedListRequestDto.getSearch().trim().equals("")) {
           sickbeds = sickbedRepository.findByRoom_RoomType_RoomTypeAndSickbedStatus(sickbedListRequestDto.getRoomType(), SickbedStatus.EMPTY, pageable);
        } else {
            String[] parts = sickbedListRequestDto.getSearch().split("-");
            if (parts.length>4) {
                throw new ApiException(111,"Xin vui lòng nhập đúng cú pháp: Tòa nhà-số tầng-số phòng-số giường");
            } else {
                if (parts.length==1 && !parts[0].equals("")) {
                    sickbeds = sickbedRepository.findByRoom_RoomType_RoomTypeAndRoom_Floor_Building_BuildingNameAndSickbedStatus(sickbedListRequestDto.getRoomType(), parts[0], SickbedStatus.EMPTY, pageable);
                } else if (parts.length==2 && !parts[0].equals("")  && !parts[1].equals("") ) {
                    sickbeds = sickbedRepository.findByRoom_RoomType_RoomTypeAndRoom_Floor_Building_BuildingNameAndRoom_Floor_FloorNoAndSickbedStatus(sickbedListRequestDto.getRoomType(), parts[0], parts[1], SickbedStatus.EMPTY, pageable);
                } else if (parts.length==3 && !parts[0].equals("")  && !parts[1].equals("")  && !parts[2].equals("")) {
                    sickbeds = sickbedRepository.findByRoom_RoomType_RoomTypeAndRoom_Floor_Building_BuildingNameAndRoom_Floor_FloorNoAndRoom_RoomNoAndSickbedStatus(sickbedListRequestDto.getRoomType(), parts[0], parts[1], parts[2], SickbedStatus.EMPTY, pageable);
                } else if (parts.length==4 && !parts[0].equals("")  && !parts[1].equals("")  && !parts[2].equals("")  && !parts[3].equals("")){
                    sickbeds = sickbedRepository.findByRoom_RoomType_RoomTypeAndRoom_Floor_Building_BuildingNameAndRoom_Floor_FloorNoAndRoom_RoomNoAndSickBedNoAndSickbedStatus(sickbedListRequestDto.getRoomType(), parts[0], parts[1], parts[2], parts[3], SickbedStatus.EMPTY, pageable);
                } else {
                    throw new ApiException(111,"Không tìm thấy giường bệnh trống");
                }
            }
        }
        return PageResponse.buildPageResponse(sickbeds.map(commonMapper::mapSickbedToPatientLocationResponseDto));
    }

    @Override
    public List<SickbedDto> getSickbedList(RoomDto roomDto) {
        Optional<Building> building = buildingRepository.findById(roomDto.getBuildingName());
        if (building.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.BUILDING_NOT_FOUND);
        }
        Optional<Floor> floor = floorRepository.findById(new FloorPK(roomDto.getFloorNo(), roomDto.getBuildingName()));
        if (floor.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.FLOOR_NOT_FOUND);
        }

        Optional<Room> room = roomRepository.findById(new RoomPK(roomDto.getRoomNo(), floor.get()));
        if (room.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.ROOM_NOT_FOUND);
        }
        return sickbedRepository.findByRoom_Floor_Building_BuildingNameAndRoom_Floor_FloorNoAndRoom_RoomNo
                (roomDto.getBuildingName(), roomDto.getFloorNo(), roomDto.getRoomNo()).stream().map(x -> sickbedMapper.mapSickbedToSickbedDto(x)).collect(Collectors.toList());
    }
}

