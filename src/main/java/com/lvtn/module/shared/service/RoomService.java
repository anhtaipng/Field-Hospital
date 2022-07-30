package com.lvtn.module.shared.service;

import com.lvtn.module.shared.dto.FloorDto;
import com.lvtn.module.shared.dto.PatientLocationDto;
import com.lvtn.module.shared.dto.RoomDto;

import java.util.List;

public interface RoomService {
    int addRoom(RoomDto roomDto);
    int updateRoom(RoomDto roomDto);
    void deleteRoom(RoomDto roomDto);
    List<RoomDto> getRoomList (FloorDto floorDto);
}
