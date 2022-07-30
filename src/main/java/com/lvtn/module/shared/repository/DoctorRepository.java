package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.Doctor;
import com.lvtn.module.shared.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor,Long> {
    Doctor findDoctorByUser(User user);
    Doctor findDoctorByUser_Username(String username);
    Page<Doctor> findByUser_UsernameContaining(String username, Pageable pageable);
    Page<Doctor> findByUser_NameContaining(String username, Pageable pageable);
    List<Doctor> findByShift(String shift);
}
