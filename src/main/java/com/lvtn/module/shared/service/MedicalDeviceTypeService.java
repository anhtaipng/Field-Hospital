package com.lvtn.module.shared.service;

import com.lvtn.module.shared.model.MedicalDeviceType;

import java.util.List;

public interface MedicalDeviceTypeService {
    List<MedicalDeviceType> getMedicalDeviceTypeList();
    MedicalDeviceType addMedicalDeviceType(MedicalDeviceType medicalDeviceType);
    void deleteMedicalDeviceType(MedicalDeviceType medicalDeviceType);
}
