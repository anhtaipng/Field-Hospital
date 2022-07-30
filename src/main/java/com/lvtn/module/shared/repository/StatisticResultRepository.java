package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.StatisticResult;
import com.lvtn.module.shared.model.TestResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatisticResultRepository extends JpaRepository<StatisticResult,Long> {
    List<StatisticResult> findAllByPatient_Id(Long id);
    List<StatisticResult> findAllByPatient_User_Username(String username);
    Page<StatisticResult> findByStatisticType_StatisticTypeAndPatient_User_UsernameOrderByTimeCreatedDesc(String statisticType, String username, Pageable pageable);
    List<StatisticResult> findAllByStatisticType_StatisticTypeAndPatient_User_UsernameOrderByTimeCreatedDesc(String statisticType,String username);
    StatisticResult findFirstByStatisticType_StatisticTypeAndPatient_User_UsernameOrderByTimeCreatedDesc(String statisticType,String username);
}
