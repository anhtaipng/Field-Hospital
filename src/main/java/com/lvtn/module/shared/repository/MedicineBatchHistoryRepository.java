package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.Medicine;
import com.lvtn.module.shared.model.MedicineBatch;
import com.lvtn.module.shared.model.MedicineBatchHistory;
import com.lvtn.module.shared.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MedicineBatchHistoryRepository extends JpaRepository<MedicineBatchHistory,Long> {
    List<MedicineBatchHistory> findByPrescription(Prescription prescription);
}
