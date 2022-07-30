package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.enumeration.SickbedStatus;
import com.lvtn.module.shared.model.Prescription;
import com.lvtn.module.shared.model.Sickbed;
import com.lvtn.module.shared.model.SickbedPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SickbedRepository extends JpaRepository<Sickbed, SickbedPK> {
    @Modifying
    @Query(value = "insert into sickbed (sickbed_no, room_no, floor_no, building_name, sickbed_status) " +
            "values (:sickbedNo,:roomNo,:floorNo,:buildingName, :sickbedStatus)",nativeQuery = true)
    @Transactional
    int saveSickbed(String sickbedNo, String roomNo, String floorNo, String buildingName, String sickbedStatus);

    @Modifying
    @Query(value = "update sickbed set sickbed_no = :newSickbedNo," +
            "room_no=:newRoomNo, floor_no=:newFloorNo, building_name=:newBuildingName,sickbed_status=:newSickbedStatus " +
            "where sickbed_no=:oldSickbedNo and room_no=:oldRoomNo and floor_no=:oldFloorNo and building_name=:oldBuildingName",nativeQuery = true)
    @Transactional
    int updateSickbed(String newSickbedNo, String newRoomNo, String newFloorNo, String newBuildingName, String newSickbedStatus,
                      String oldSickbedNo, String oldRoomNo, String oldFloorNo, String oldBuildingName);

    @Modifying
    @Query(value = "update sickbed set sickbed_status=:newSickbedStatus " +
            "where sickbed_no=:oldSickbedNo and room_no=:oldRoomNo and floor_no=:oldFloorNo and building_name=:oldBuildingName",nativeQuery = true)
    @Transactional
    int updateSickbedStatus(String newSickbedStatus, String oldSickbedNo, String oldRoomNo, String oldFloorNo, String oldBuildingName);

    @Modifying
    @Query(value = "delete from sickbed " +
            "where sickbed_no=:oldSickbedNo and room_no=:oldRoomNo and floor_no=:oldFloorNo and building_name=:oldBuildingName",nativeQuery = true)
    @Transactional
    int deleteSickbed(String oldSickbedNo, String oldRoomNo, String oldFloorNo, String oldBuildingName);

    Page<Sickbed> findBySickbedStatus(String sickbedStatus,Pageable pageable);
    Page<Sickbed> findByRoom_RoomType_RoomTypeAndSickbedStatus(String roomType, SickbedStatus sickbedStatus,Pageable pageable);
    Page<Sickbed> findByRoom_RoomType_RoomTypeAndRoom_Floor_Building_BuildingNameAndSickbedStatus(String roomType,String buildingName, SickbedStatus sickbedStatus,Pageable pageable);
    Page<Sickbed> findByRoom_RoomType_RoomTypeAndRoom_Floor_Building_BuildingNameAndRoom_Floor_FloorNoAndSickbedStatus(String roomType,String buildingName, String floorNo, SickbedStatus sickbedStatus,Pageable pageable);
    Page<Sickbed> findByRoom_RoomType_RoomTypeAndRoom_Floor_Building_BuildingNameAndRoom_Floor_FloorNoAndRoom_RoomNoAndSickbedStatus(String roomType,String buildingName, String floorNo, String roomNo, SickbedStatus sickbedStatus,Pageable pageable);
    Page<Sickbed> findByRoom_RoomType_RoomTypeAndRoom_Floor_Building_BuildingNameAndRoom_Floor_FloorNoAndRoom_RoomNoAndSickBedNoAndSickbedStatus(String roomType,String buildingName, String floorNo, String roomNo,  String sickbedNo, SickbedStatus sickbedStatus,Pageable pageable);


    List<Sickbed> findByRoom_Floor_Building_BuildingNameAndSickbedStatus(String buildingName, SickbedStatus sickbedStatus);
    List<Sickbed> findByRoom_Floor_Building_BuildingNameAndRoom_Floor_FloorNoAndSickbedStatus(String buildingName, String floorNo, SickbedStatus sickbedStatus);
    List<Sickbed> findByRoom_Floor_Building_BuildingNameAndRoom_Floor_FloorNoAndRoom_RoomNoAndSickbedStatus(String buildingName, String floorNo, String roomNo, SickbedStatus sickbedStatus);
    List<Sickbed> findByRoom_Floor_Building_BuildingNameAndRoom_Floor_FloorNoAndRoom_RoomNo(String buildingName, String floorNo, String roomNo);
}
