package com.lvtn.module.shared.service;

import com.lvtn.module.shared.model.Building;
import com.lvtn.module.shared.model.RoomType;

import java.util.List;

public interface RoomTypeService {
    RoomType addRoomType(RoomType roomType);
    RoomType updateRoomType(RoomType roomType);
    List<RoomType> getRoomType();
    void deleteRoomType(RoomType roomType);
}
