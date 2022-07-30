package com.lvtn.module.shared.mapper;

import com.lvtn.module.shared.dto.*;
import com.lvtn.module.shared.enumeration.SickbedStatus;
import com.lvtn.module.shared.model.*;
import com.lvtn.module.shared.repository.BuildingRepository;
import com.lvtn.module.shared.repository.FloorRepository;
import com.lvtn.module.shared.repository.RoomRepository;
import com.lvtn.module.shared.repository.SickbedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SickbedMapper {

    @Autowired
    SickbedRepository sickbedRepository;

    public SickbedPK mapSickbedToSickbedPK(Sickbed sickbed){
        SickbedPK sickbedPK = new SickbedPK();
        sickbedPK.setSickBedNo(sickbed.getSickBedNo());
        sickbedPK.setRoom(sickbed.getRoom());
        return sickbedPK;
    }

    public SickbedPK mapPatientLocationToSickbedPK(PatientLocation patientLocation){
        SickbedPK sickbedPK = new SickbedPK();
        sickbedPK.setSickBedNo(patientLocation.getSickbed().getSickBedNo());
        sickbedPK.setRoom(patientLocation.getSickbed().getRoom());
        return sickbedPK;
    }

    public SickbedDto mapSickbedToSickbedDto(Sickbed sickbed){
        SickbedDto sickbedDto = new SickbedDto();
        sickbedDto.setSickbedNo(sickbed.getSickBedNo());
        sickbedDto.setFloorNo(sickbed.getRoom().getFloor().getFloorNo());
        sickbedDto.setBuildingName(sickbed.getRoom().getFloor().getBuilding().getBuildingName());
        sickbedDto.setRoomNo(sickbed.getRoom().getRoomNo());
        sickbedDto.setSickbedStatus(sickbed.getSickbedStatus());
        return sickbedDto;
    }

    public Sickbed mapSickbedDtoToSickbed(SickbedDto sickbedDto,Room room){
        Sickbed sickbed = new Sickbed();
        sickbed.setRoom(room);
        sickbed.setSickBedNo(sickbedDto.getSickbedNo());
        sickbed.setSickbedStatus(sickbedDto.getSickbedStatus());
        return sickbed;
    }

    public Room mapRoomDtoToRoom(RoomDto roomDto,Floor floor,RoomType roomType){
        Room room = new Room();
        room.setRoomNo(roomDto.getRoomNo());
        room.setRoomType(roomType);
        room.setFloor(floor);
        return room;
    }

    public RoomDto mapRoomToRoomDto(Room room){
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomNo(room.getRoomNo());
        roomDto.setRoomType(room.getRoomType().getRoomType());
        roomDto.setBuildingName(room.getFloor().getBuilding().getBuildingName());
        roomDto.setFloorNo(room.getFloor().getFloorNo());

        List<Sickbed> emptySickbeds = sickbedRepository.findByRoom_Floor_Building_BuildingNameAndRoom_Floor_FloorNoAndRoom_RoomNoAndSickbedStatus
                (roomDto.getBuildingName(), roomDto.getFloorNo(), roomDto.getRoomNo(), SickbedStatus.EMPTY);
        List<Sickbed> usedSickbeds = sickbedRepository.findByRoom_Floor_Building_BuildingNameAndRoom_Floor_FloorNoAndRoom_RoomNoAndSickbedStatus
                (roomDto.getBuildingName(), roomDto.getFloorNo(), roomDto.getRoomNo(), SickbedStatus.USED);
        List<Sickbed> requetedSickbeds = sickbedRepository.findByRoom_Floor_Building_BuildingNameAndRoom_Floor_FloorNoAndRoom_RoomNoAndSickbedStatus
                (roomDto.getBuildingName(), roomDto.getFloorNo(), roomDto.getRoomNo(), SickbedStatus.REQUESTED);
        List<Sickbed> disableSickbeds = sickbedRepository.findByRoom_Floor_Building_BuildingNameAndRoom_Floor_FloorNoAndRoom_RoomNoAndSickbedStatus
                (roomDto.getBuildingName(), roomDto.getFloorNo(), roomDto.getRoomNo(), SickbedStatus.DISABLE);
        roomDto.setEmptySickbed(emptySickbeds.size());
        roomDto.setUsedSickbed(usedSickbeds.size());
        roomDto.setRequestedSickbed(requetedSickbeds.size());
        roomDto.setDisableSickbed(disableSickbeds.size());

        return roomDto;
    }

    public Floor mapFloorDtoToFloor(FloorDto floorDto, Building building){
        Floor floor = new Floor();
        floor.setBuilding(building);
        floor.setFloorNo(floorDto.getFloorNo());
        return floor;
    }

    public FloorDto mapFloorToFloorDto(Floor floor){
        FloorDto floorDto = new FloorDto();
        floorDto.setFloorNo(floor.getFloorNo());
        floorDto.setBuildingName(floor.getBuilding().getBuildingName());

        List<Sickbed> emptySickbeds = sickbedRepository.findByRoom_Floor_Building_BuildingNameAndRoom_Floor_FloorNoAndSickbedStatus(floorDto.getBuildingName(), floorDto.getFloorNo(), SickbedStatus.EMPTY);
        List<Sickbed> usedSickbeds = sickbedRepository.findByRoom_Floor_Building_BuildingNameAndRoom_Floor_FloorNoAndSickbedStatus(floorDto.getBuildingName(), floorDto.getFloorNo(), SickbedStatus.USED);
        List<Sickbed> requetedSickbeds = sickbedRepository.findByRoom_Floor_Building_BuildingNameAndRoom_Floor_FloorNoAndSickbedStatus(floorDto.getBuildingName(), floorDto.getFloorNo(), SickbedStatus.REQUESTED);
        List<Sickbed> disableSickbeds = sickbedRepository.findByRoom_Floor_Building_BuildingNameAndRoom_Floor_FloorNoAndSickbedStatus(floorDto.getBuildingName(), floorDto.getFloorNo(), SickbedStatus.DISABLE);
        floorDto.setEmptySickbed(emptySickbeds.size());
        floorDto.setUsedSickbed(usedSickbeds.size());
        floorDto.setRequestedSickbed(requetedSickbeds.size());
        floorDto.setDisableSickbed(disableSickbeds.size());

        return floorDto;
    }

    public Building mapBuildingDtoToBuilding(BuildingDto buildingDto){
        Building building = new Building();
        building.setBuildingName(buildingDto.getBuildingName());
        return building;
    }

    public BuildingDto mapBuildingToBuildingDto(Building building){
        BuildingDto buildingDto = new BuildingDto();
        buildingDto.setBuildingName(building.getBuildingName());

        List<Sickbed> emptySickbeds = sickbedRepository.findByRoom_Floor_Building_BuildingNameAndSickbedStatus(buildingDto.getBuildingName(), SickbedStatus.EMPTY);
        List<Sickbed> usedSickbeds = sickbedRepository.findByRoom_Floor_Building_BuildingNameAndSickbedStatus(buildingDto.getBuildingName(), SickbedStatus.USED);
        List<Sickbed> requetedSickbeds = sickbedRepository.findByRoom_Floor_Building_BuildingNameAndSickbedStatus(buildingDto.getBuildingName(), SickbedStatus.REQUESTED);
        List<Sickbed> disableSickbeds = sickbedRepository.findByRoom_Floor_Building_BuildingNameAndSickbedStatus(buildingDto.getBuildingName(), SickbedStatus.DISABLE);
        buildingDto.setEmptySickbed(emptySickbeds.size());
        buildingDto.setUsedSickbed(usedSickbeds.size());
        buildingDto.setRequestedSickbed(requetedSickbeds.size());
        buildingDto.setDisableSickbed(disableSickbeds.size());

        return buildingDto;
    }

}
