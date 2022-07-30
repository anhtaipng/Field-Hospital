package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AllergyRepisitory extends JpaRepository<Allergy,Long> {
    List<Allergy> findByPatient_User_Username(String username);
}
