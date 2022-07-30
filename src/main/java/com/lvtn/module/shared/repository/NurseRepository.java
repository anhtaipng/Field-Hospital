package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.Nurse;
import com.lvtn.module.shared.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NurseRepository extends JpaRepository<Nurse,Long> {
    Nurse findNurseByUser(User user);
    Nurse findNurseByUser_Username(String username);
    Page<Nurse> findByUser_UsernameContaining(String username, Pageable pageable);
    Page<Nurse> findByUser_NameContaining(String name, Pageable pageable);
    List<Nurse> findByShift(String shift);
}
