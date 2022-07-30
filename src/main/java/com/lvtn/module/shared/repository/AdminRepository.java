package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.Admin;
import com.lvtn.module.shared.model.Doctor;
import com.lvtn.module.shared.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,Long> {
    Admin findAdminByUser(User user);
    Admin findAdminByUser_Username(String username);
}
