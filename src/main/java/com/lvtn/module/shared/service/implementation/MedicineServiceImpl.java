package com.lvtn.module.shared.service.implementation;

import com.google.common.base.Strings;
import com.lvtn.module.admin.common.ApiAdminMesssage;
import com.lvtn.module.shared.dto.MedicineDto;
import com.lvtn.module.shared.dto.MedicineListRequestDto;
import com.lvtn.module.shared.mapper.MedicineMapper;
import com.lvtn.module.shared.model.Medicine;
import com.lvtn.module.shared.model.MedicineBatch;
import com.lvtn.module.shared.repository.MedicineBatchRepository;
import com.lvtn.module.shared.repository.MedicineRepository;
import com.lvtn.module.shared.service.MedicineService;
import com.lvtn.platform.common.PageResponse;
import com.lvtn.platform.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MedicineServiceImpl implements MedicineService {
    @Autowired
    MedicineRepository medicineRepository;

    @Autowired
    MedicineBatchRepository medicineBatchRepository;

    @Autowired
    MedicineMapper medicineMapper;

    @Override
    public PageResponse<MedicineDto> getMedicineList(MedicineListRequestDto medicineListRequestDto) {
        Page<Medicine> medicines;
        Pageable pageable = PageRequest.of(medicineListRequestDto.getPageNum(), medicineListRequestDto.getPageSize());

        if (Strings.isNullOrEmpty((medicineListRequestDto.getKeyword()))){
            medicines = medicineRepository.findAll(pageable);
        }
        else{
            medicines = medicineRepository.findByMedicineNameContaining(medicineListRequestDto.getKeyword(), pageable);
        }
        return PageResponse.buildPageResponse((medicines.map(medicineMapper::mapMedicineToMedicineDto)));
    }

    @Override
    public List<MedicineDto> getMedicineList() {
        return medicineRepository.findAll().stream().map(medicineMapper::mapMedicineToMedicineDto).collect(Collectors.toList());
    }

    @Override
    public Medicine addMedicine(Medicine medicine) {
        if (medicineRepository.findByMedicineName(medicine.getMedicineName()) != null) {
            throw new ApiException(ApiAdminMesssage.MEDICINE_EXISTED);
        }
        return medicineRepository.save(medicine);
    }


    @Override
    public Medicine updateMedicine(Medicine medicine) {
        if (medicineRepository.findByMedicineName(medicine.getMedicineName()) == null) {
            throw new ApiException(ApiAdminMesssage.MEDICINE_NOT_FOUND);
        }
        return medicineRepository.save(medicine);
    }

    @Override
    public void deleteMedicine(Medicine medicine) {
        if (medicineRepository.findByMedicineName(medicine.getMedicineName()) == null) {
            throw new ApiException(ApiAdminMesssage.MEDICINE_NOT_FOUND);
        }
        medicineRepository.delete(medicine);
    }

    @Override
    public Long getAvailableQuantityMedicine(String medicineName) {
        Long result = Long.valueOf(0);
        List<MedicineBatch> medicineBatches = medicineBatchRepository.findByMedicineName_medicineName(medicineName);
        for (MedicineBatch x : medicineBatches){
            result += x.getAvailableQuantity();
        }
        return result;
    }


}
