package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.Surgery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurgeryRepository extends JpaRepository<Surgery, Long> {
    List<Surgery> findByPatient_User_Username(String username);
}
