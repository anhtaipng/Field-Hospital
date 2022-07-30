package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.Dependent;
import com.lvtn.module.shared.model.DependentPK;
import com.lvtn.module.shared.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DependentRepository extends JpaRepository<Dependent, DependentPK> {
}
