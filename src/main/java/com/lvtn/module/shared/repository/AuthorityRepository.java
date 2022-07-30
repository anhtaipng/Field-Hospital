package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority,Long> {
    Authority findAuthorityByRoleName(String roleName);
}
