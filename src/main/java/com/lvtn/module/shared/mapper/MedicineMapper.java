package com.lvtn.module.shared.mapper;

import com.lvtn.module.shared.dto.MedicineDto;
import com.lvtn.module.shared.model.Medicine;
import com.lvtn.module.shared.repository.MedicineBatchRepository;
import com.lvtn.module.shared.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MedicineMapper {
    @Autowired
    MedicineRepository medicineRepository;

    @Autowired
    MedicineBatchRepository medicineBatchRepository;

    public MedicineDto mapMedicineToMedicineDto(Medicine medicine){
        MedicineDto medicineDto = new MedicineDto();
        medicineDto.setMedicineName(medicine.getMedicineName());
        medicineDto.setUnit(medicine.getUnit());
        return medicineDto;
    }
}
