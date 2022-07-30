package com.lvtn.module.shared.service;

import com.lvtn.module.shared.dto.MedicineDto;
import com.lvtn.module.shared.dto.MedicineListRequestDto;
import com.lvtn.module.shared.model.Medicine;
import com.lvtn.platform.common.PageResponse;

import java.util.List;

public interface MedicineService {
    Medicine addMedicine(Medicine medicine);
    PageResponse<MedicineDto> getMedicineList(MedicineListRequestDto medicineListRequestDto);
    List<MedicineDto> getMedicineList();
    Medicine updateMedicine(Medicine medicine);
    void deleteMedicine(Medicine medicine);
    Long getAvailableQuantityMedicine(String medicineName);
}
