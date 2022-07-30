package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.ImportPatient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImportPatientRepository extends JpaRepository<ImportPatient, Long> {
    List<ImportPatient> findByIsAddedIsFalse();
}
