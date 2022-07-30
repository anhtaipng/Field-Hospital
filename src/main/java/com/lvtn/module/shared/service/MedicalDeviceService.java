package com.lvtn.module.shared.service;

import com.lvtn.module.shared.dto.MedicalDeviceDto;
import com.lvtn.module.shared.dto.MedicalDeviceListRequestDto;
import com.lvtn.platform.common.PageResponse;

public interface MedicalDeviceService {
    PageResponse<MedicalDeviceDto> getMedicalDeviceList(MedicalDeviceListRequestDto medicalDeviceListRequestDto);
    int addMedicalDevice(MedicalDeviceDto medicalDeviceDto);
    int updateMedicalDevice(MedicalDeviceDto medicalDeviceDto);
    void deleteMedicalDevice(MedicalDeviceDto medicalDeviceDto);
}
