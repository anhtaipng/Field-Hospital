package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.enumeration.PatientStatusType;
import com.lvtn.module.shared.model.PatientStatus;
import com.lvtn.module.shared.model.TestType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientStatusRepository extends JpaRepository<PatientStatus, PatientStatusType> {
}
