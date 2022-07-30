package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.BvdcGroup;
import com.lvtn.module.shared.model.Examination;
import com.lvtn.module.shared.model.Patient;
import com.lvtn.module.shared.model.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription,Long> {
    Prescription findFirstByPatientOrderByCreateTimeDesc(Patient patient);
    List<Prescription> findByPatient_User_Username(String username);
    Page<Prescription> findAllByPatient_BvdcGroupInAndTookAndPatient_User_UsernameContainingOrderByCreateTimeAsc(List<BvdcGroup> bvdcGroup, Boolean took, String username, Pageable pageable);
    Page<Prescription> findAllByPatient_BvdcGroupInAndTookAndPatient_User_NameContainingOrderByCreateTimeAsc(List<BvdcGroup> bvdcGroup, Boolean took, String name, Pageable pageable);
    Page<Prescription> findAllByPatient_BvdcGroupInAndTookOrderByCreateTimeAsc(List<BvdcGroup> bvdcGroup, Boolean took ,Pageable pageable);
}
