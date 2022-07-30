package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.CovidHospital;
import com.lvtn.module.shared.model.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CovidHospitalRepository extends JpaRepository<CovidHospital,String> {
    List<CovidHospital> findAllByEnableTrue();
}
