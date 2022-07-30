package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.enumeration.PatientStatusType;
import com.lvtn.module.shared.model.BvdcGroup;
import com.lvtn.module.shared.model.Patient;
import com.lvtn.module.shared.model.PatientStatus;
import com.lvtn.module.shared.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient,Long> {
    Patient findPatientByUser_Username(String username);
    Page<Patient> findByBvdcGroup(BvdcGroup bvdcGroup, Pageable pageable);
    long countByBvdcGroup(BvdcGroup bvdcGroup);
    Page<Patient> findByBvdcGroupAndUser_UsernameContaining(BvdcGroup bvdcGroup, String username, Pageable pageable);
    Page<Patient> findAllByBvdcGroupInAndUser_UsernameContaining(List<BvdcGroup> bvdcGroup, String username, Pageable pageable);
    Page<Patient> findByBvdcGroupAndUser_NameContaining(BvdcGroup bvdcGroup, String name, Pageable pageable);
    Page<Patient> findAllByBvdcGroupInAndUser_NameContaining(List<BvdcGroup> bvdcGroup, String name, Pageable pageable);
    List<Patient> findByBvdcGroup(BvdcGroup bvdcGroup);
    Page<Patient> findByUser_UsernameContaining(String username, Pageable pageable);
    Page<Patient> findByUser_NameContaining(String username, Pageable pageable);
    Patient findPatientById(Long id);
    List<Patient> findAllByBvdcGroupIn(List<BvdcGroup> bvdcGroup);
    List<Patient> findAllByPatientStatus_PatientStatusTypeAndBvdcGroupIn(PatientStatusType patientStatusType, List<BvdcGroup> bvdcGroup);
    Page<Patient> findAllByBvdcGroupInOrderById(List<BvdcGroup> bvdcGroup, Pageable pageable);
}
