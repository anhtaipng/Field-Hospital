package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.Medicine;
import com.lvtn.module.shared.model.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine,String> {
    Page<Medicine> findByMedicineNameContaining(String medicineName, Pageable pageable);
    Medicine findByMedicineName(String medicineName);
}
