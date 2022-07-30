package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.enumeration.RequirementStatus;
import com.lvtn.module.shared.model.MedicineRequirement;
import com.lvtn.module.shared.model.Requirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineRequirementRepository extends JpaRepository<MedicineRequirement,Long> {
    Page<MedicineRequirement> findByDoctor_User_Username(String username, Pageable pageable);
    Page<MedicineRequirement> findByStatusAndDoctor_User_UsernameOrderByRequirementTimeDesc(RequirementStatus status, String username, Pageable pageable);
}
