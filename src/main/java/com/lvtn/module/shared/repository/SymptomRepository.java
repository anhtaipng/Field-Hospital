package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.Examination;
import com.lvtn.module.shared.model.Symptom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SymptomRepository extends JpaRepository<Symptom,String> {
}
