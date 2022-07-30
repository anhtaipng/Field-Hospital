package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.MedicalDevice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface MedicalDeviceRepository extends JpaRepository<MedicalDevice, Long> {
    Page<MedicalDevice> findByMedicalDeviceType_MedicalDeviceTypeContaining(String medicalDeviceType, Pageable pageable);

    @Modifying
    @Query(value = "insert into medical_device (medical_device_type, medical_device_number, building_name, floor_no, room_no, sickbed_no) " +
            "values (:medicalDeviceType,:medicalDeviceNumber,:buildingName,:floorNo, :roomNo, :sickbedNo)", nativeQuery = true)
    @Transactional
    int saveMedicalDevice(String medicalDeviceType, String medicalDeviceNumber, String buildingName, String floorNo, String roomNo, String sickbedNo);

    @Modifying
    @Query(value = "update medical_device set medical_device_type=:medicalDeviceType, medical_device_number=:medicalDeviceNumber," +
            "building_name=:buildingName, room_no=:roomNo, floor_no=:floorNo, sickbed_no=:sickbedNo where id=:id", nativeQuery = true)
    @Transactional
    int updateMedicalDevice(Long id, String medicalDeviceType, String medicalDeviceNumber, String buildingName, String floorNo, String roomNo, String sickbedNo);

    @Modifying
    @Query(value = "delete from medical_device where id=:id", nativeQuery = true)
    @Transactional
    int deleteMedicalDevice(Long id);
}
