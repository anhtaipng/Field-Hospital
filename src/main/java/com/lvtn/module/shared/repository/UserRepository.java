package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findUserByUsername(String username);
}
