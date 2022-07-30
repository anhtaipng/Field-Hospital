package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.Requirement;
import com.lvtn.module.shared.model.TestType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestTypeRepository extends JpaRepository<TestType,String> {
    TestType findByTestType(String testType);
}
