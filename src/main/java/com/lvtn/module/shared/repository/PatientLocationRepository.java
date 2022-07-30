package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.Patient;
import com.lvtn.module.shared.model.PatientLocation;
import com.lvtn.module.shared.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientLocationRepository extends JpaRepository<PatientLocation,Long> {
    List<PatientLocation> findByPatient_User_Username(String username);
    PatientLocation findByPatient_User_UsernameAndEndTimeIsNull(String username);
    PatientLocation findByPatientAndEndTimeIsNull(Patient patient);
    List<PatientLocation> findByEndTimeIsNotNull();
}
