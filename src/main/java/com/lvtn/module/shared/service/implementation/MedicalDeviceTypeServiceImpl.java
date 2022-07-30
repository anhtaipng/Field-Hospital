package com.lvtn.module.shared.service.implementation;

import com.lvtn.module.shared.common.ApiSharedMesssage;
import com.lvtn.module.shared.model.MedicalDeviceType;
import com.lvtn.module.shared.repository.MedicalDeviceTypeRepository;
import com.lvtn.module.shared.service.MedicalDeviceTypeService;
import com.lvtn.platform.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MedicalDeviceTypeServiceImpl implements MedicalDeviceTypeService {
    @Autowired
    MedicalDeviceTypeRepository medicalDeviceTypeRepository;

    @Override
    public List<MedicalDeviceType> getMedicalDeviceTypeList() {
        return medicalDeviceTypeRepository.findAll();
    }

    @Override
    public MedicalDeviceType addMedicalDeviceType(MedicalDeviceType medicalDeviceType) {
        if (medicalDeviceTypeRepository.findById(medicalDeviceType.getMedicalDeviceType()).isPresent()){
            throw new ApiException(ApiSharedMesssage.MEDICAL_DEVICE_TYPE_EXISTED);
        }
        return medicalDeviceTypeRepository.save(medicalDeviceType);
    }

    @Override
    public void deleteMedicalDeviceType(MedicalDeviceType medicalDeviceType) {
        if (medicalDeviceTypeRepository.findById(medicalDeviceType.getMedicalDeviceType()).isEmpty()){
            throw new ApiException(ApiSharedMesssage.MEDICAL_DEVICE_TYPE_NOT_FOUND);
        }
        medicalDeviceTypeRepository.delete(medicalDeviceType);
    }
}
