package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.Medicine;
import com.lvtn.module.shared.model.MedicineBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface MedicineBatchRepository extends JpaRepository<MedicineBatch,Long> {
    MedicineBatch findByMedicineNameAndAvailableQuantityGreaterThanOrderByExpiredDateDesc(Medicine medicine,Long availableQuantity);
    List<MedicineBatch> findByMedicineName_medicineName(String medicineName);
    MedicineBatch findFirstByMedicineNameAndAvailableQuantityGreaterThanAndExpiredDateAfterOrderByExpiredDateAsc(Medicine medicine, Long availableQuantity, Date currentDate);
    MedicineBatch findByBatchNumber(String batchNumber);
}
