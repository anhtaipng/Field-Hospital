package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.Medicine;
import com.lvtn.module.shared.model.Prescription;
import com.lvtn.module.shared.model.PrescriptionDetail;
import com.lvtn.module.shared.model.PrescriptionDetailPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionDetailRepository extends JpaRepository<PrescriptionDetail, PrescriptionDetailPK> {
    List<PrescriptionDetail> findByPrescription(Prescription prescription);
}
