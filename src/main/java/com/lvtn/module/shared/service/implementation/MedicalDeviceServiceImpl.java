package com.lvtn.module.shared.service.implementation;

import com.google.common.base.Strings;
import com.lvtn.module.shared.common.ApiSharedMesssage;
import com.lvtn.module.shared.dto.MedicalDeviceDto;
import com.lvtn.module.shared.dto.MedicalDeviceListRequestDto;
import com.lvtn.module.shared.mapper.CommonMapper;
import com.lvtn.module.shared.mapper.SickbedMapper;
import com.lvtn.module.shared.model.*;
import com.lvtn.module.shared.repository.*;
import com.lvtn.module.shared.service.MedicalDeviceService;
import com.lvtn.platform.common.PageResponse;
import com.lvtn.platform.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MedicalDeviceServiceImpl implements MedicalDeviceService {
    @Autowired
    MedicalDeviceRepository medicalDeviceRepository;

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
    public PageResponse<MedicalDeviceDto> getMedicalDeviceList(MedicalDeviceListRequestDto medicalDeviceListRequestDto) {
        Page<MedicalDevice> medicalDevices;
        Pageable pageable = PageRequest.of(medicalDeviceListRequestDto.getPageNum(), medicalDeviceListRequestDto.getPageSize());

        if (Strings.isNullOrEmpty(medicalDeviceListRequestDto.getKeyword())) {
            medicalDevices = medicalDeviceRepository.findAll(pageable);
        } else {
            medicalDevices = medicalDeviceRepository.findByMedicalDeviceType_MedicalDeviceTypeContaining(medicalDeviceListRequestDto.getKeyword(), pageable);
        }
        return PageResponse.buildPageResponse(medicalDevices.map(commonMapper::mapMedicalDeviceToMedicalDeviceDto));
    }

    @Override
    public int addMedicalDevice(MedicalDeviceDto medicalDeviceDto) {
        Optional<Building> building = buildingRepository.findById(medicalDeviceDto.getBuildingName());
        if (building.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.BUILDING_NOT_FOUND);
        }
        Optional<Floor> floor = floorRepository.findById(new FloorPK(medicalDeviceDto.getFloorNo(), medicalDeviceDto.getBuildingName()));
        if (floor.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.FLOOR_NOT_FOUND);
        }

        Optional<Room> room = roomRepository.findById(new RoomPK(medicalDeviceDto.getRoomNo(), floor.get()));
        if (room.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.ROOM_NOT_FOUND);
        }

        Optional<Sickbed> sickbed = sickbedRepository.findById(new SickbedPK(medicalDeviceDto.getSickbedNo(), room.get()));
        if (sickbed.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.SICKBED_NOT_FOUND);
        }

        Optional<MedicalDevice> medicalDevice = medicalDeviceRepository.findById(medicalDeviceDto.getId());
        if (medicalDevice.isPresent()) {
            throw new ApiException(13123,"Thiết bị đã tồn tại");
        }

        return medicalDeviceRepository.saveMedicalDevice(medicalDeviceDto.getMedicalDeviceType(), medicalDeviceDto.getMedicalDeviceNumber(),
                medicalDeviceDto.getBuildingName(), medicalDeviceDto.getFloorNo(), medicalDeviceDto.getRoomNo(), medicalDeviceDto.getSickbedNo());
    }

    @Override
    public int updateMedicalDevice(MedicalDeviceDto medicalDeviceDto) {
        Optional<Building> building = buildingRepository.findById(medicalDeviceDto.getBuildingName());
        if (building.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.BUILDING_NOT_FOUND);
        }
        Optional<Floor> floor = floorRepository.findById(new FloorPK(medicalDeviceDto.getFloorNo(), medicalDeviceDto.getBuildingName()));
        if (floor.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.FLOOR_NOT_FOUND);
        }

        Optional<Room> room = roomRepository.findById(new RoomPK(medicalDeviceDto.getRoomNo(), floor.get()));
        if (room.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.ROOM_NOT_FOUND);
        }

        Optional<Sickbed> sickbed = sickbedRepository.findById(new SickbedPK(medicalDeviceDto.getSickbedNo(), room.get()));
        if (sickbed.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.SICKBED_NOT_FOUND);
        }

        Optional<MedicalDevice> medicalDevice = medicalDeviceRepository.findById(medicalDeviceDto.getId());
        if (medicalDevice.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.MEDICAL_DEVICE_TYPE_NOT_FOUND);
        }

        return medicalDeviceRepository.updateMedicalDevice(medicalDeviceDto.getId(), medicalDeviceDto.getMedicalDeviceType(), medicalDeviceDto.getMedicalDeviceNumber(),
                medicalDeviceDto.getBuildingName(), medicalDeviceDto.getFloorNo(), medicalDeviceDto.getRoomNo(), medicalDeviceDto.getSickbedNo());
    }

    @Override
    public void deleteMedicalDevice(MedicalDeviceDto medicalDeviceDto) {
        Optional<MedicalDevice> medicalDevice = medicalDeviceRepository.findById(medicalDeviceDto.getId());
        if (medicalDevice.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.MEDICAL_DEVICE_TYPE_NOT_FOUND);
        }
        medicalDeviceRepository.deleteMedicalDevice(medicalDeviceDto.getId());
    }
}
