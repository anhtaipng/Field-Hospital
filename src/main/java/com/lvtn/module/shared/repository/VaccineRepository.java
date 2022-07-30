package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.Patient;
import com.lvtn.module.shared.model.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VaccineRepository extends JpaRepository<Vaccine, Long> {
    List<Vaccine> findByPatient_User_UsernameOrderByNoInjection(String username);
    Vaccine findByNoInjectionAndPatient(Integer noInjection, Patient patient);
    long deleteByPatient(Patient patient);
}
