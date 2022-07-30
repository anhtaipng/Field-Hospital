package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.enumeration.GroupType;
import com.lvtn.module.shared.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface ExaminationRepository extends JpaRepository<Examination,Long> {
    Page<Examination> findByDoctor_User_UsernameOrderByExaminationTimeDesc(String username, Pageable pageable);
    Page<Examination> findByDoctor_User_UsernameAndExaminationTimeBetweenOrderByExaminationTimeDesc(String username, Date startTime, Date endTime, Pageable pageable);
    Page<Examination> findByPatient_User_UsernameOrderByExaminationTimeDesc(String username, Pageable pageable);
    Page<Examination> findByPatient_User_UsernameAndExaminationTimeBetweenOrderByExaminationTimeDesc(String username, Date startTime, Date endTime, Pageable pageable);
    Examination findFirstByPatientAndPrescriptionNotNullOrderByExaminationTimeDesc(Patient patient);
    Examination findByPrescription(Prescription prescription);
    List<Examination> findByPatient_User_Username(String username);
}
