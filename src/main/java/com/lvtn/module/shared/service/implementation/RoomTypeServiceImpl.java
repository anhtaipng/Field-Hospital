package com.lvtn.module.shared.service.implementation;

import com.lvtn.module.shared.common.ApiSharedMesssage;
import com.lvtn.module.shared.mapper.SickbedMapper;
import com.lvtn.module.shared.model.Building;
import com.lvtn.module.shared.model.RoomType;
import com.lvtn.module.shared.repository.BuildingRepository;
import com.lvtn.module.shared.repository.RoomTypeRepository;
import com.lvtn.module.shared.service.BuildingService;
import com.lvtn.module.shared.service.RoomTypeService;
import com.lvtn.platform.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoomTypeServiceImpl implements RoomTypeService {
    @Autowired
    RoomTypeRepository roomTypeRepository;
    @Autowired
    SickbedMapper sickbedMapper;

    @Override
    public RoomType addRoomType(RoomType roomType) {
        if (roomTypeRepository.findById(roomType.getRoomType()).isPresent()) {
            throw new ApiException(ApiSharedMesssage.ROOM_TYPE_EXISTED);
        }
        return roomTypeRepository.save(roomType);
    }

    @Override
    public RoomType updateRoomType(RoomType roomType) {
        if (roomTypeRepository.findById(roomType.getRoomType()).isEmpty()) {
            throw new ApiException(ApiSharedMesssage.ROOM_TYPE_NOT_FOUND);
        }
        return roomTypeRepository.save(roomType);
    }

    @Override
    public List<RoomType> getRoomType() {
        return roomTypeRepository.findAll();
    }

    @Override
    public void deleteRoomType(RoomType roomType) {
        if (roomTypeRepository.findById(roomType.getRoomType()).isEmpty()) {
            throw new ApiException(ApiSharedMesssage.ROOM_TYPE_NOT_FOUND);
        }
        roomTypeRepository.delete(roomType);
    }
}
