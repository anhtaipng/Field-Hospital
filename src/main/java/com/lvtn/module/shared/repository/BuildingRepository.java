package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.Building;
import com.lvtn.module.shared.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepository extends JpaRepository<Building,String> {
}
