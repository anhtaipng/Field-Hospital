package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.Floor;
import com.lvtn.module.shared.model.Room;
import com.lvtn.module.shared.model.RoomPK;
import com.lvtn.module.shared.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, RoomPK> {

    @Modifying
    @Query(value = "insert into room (room_no, floor_no, building_name, room_type) " +
            "values (:roomNo,:floorNo,:buildingName,:roomType)",nativeQuery = true)
    @Transactional
    int saveRoom(String roomNo, String floorNo, String buildingName, String roomType);

    @Modifying
    @Query(value = "update room set room_no=:newRoomNo, room_type=:newRoomType" +
            ", floor_no=:newFloorNo, building_name=:newBuildingName " +
            "where room_no=:oldRoomNo and floor_no=:oldFloorNo and building_name=:oldBuildingName",nativeQuery = true)
    @Transactional
    int updateRoom(String newRoomNo,String newRoomType, String newFloorNo, String newBuildingName,
                       String oldRoomNo, String oldFloorNo, String oldBuildingName);

    @Modifying
    @Query(value = "delete from room " +
            "where room_no=:oldRoomNo and floor_no=:oldFloorNo and building_name=:oldBuildingName",nativeQuery = true)
    @Transactional
    int deleteRoom(String oldRoomNo, String oldFloorNo, String oldBuildingName);

    List<Room> findByFloor_FloorNoAndFloor_Building_BuildingName(String floorNo, String buildingName);

}
