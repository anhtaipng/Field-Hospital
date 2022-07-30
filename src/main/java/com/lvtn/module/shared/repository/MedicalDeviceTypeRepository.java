package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.MedicalDeviceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalDeviceTypeRepository extends JpaRepository<MedicalDeviceType, String> {
}
