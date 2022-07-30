package com.lvtn.module.shared.repository;


import com.lvtn.module.shared.model.Sickness;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SicknessRepository extends JpaRepository<Sickness,Long> {
    List<Sickness> findByPatient_User_Username(String username);
}
