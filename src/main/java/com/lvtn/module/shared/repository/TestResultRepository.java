package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.model.Patient;
import com.lvtn.module.shared.model.StatisticResult;
import com.lvtn.module.shared.model.TestResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    Page<TestResult> findByPatient_User_UsernameOrderByTimeCreatedDesc(String username, Pageable pageable);
    Page<TestResult> findByTestType_TestTypeAndPatient_User_UsernameOrderByTimeCreatedDesc(String testType,String username, Pageable pageable);
    Page<TestResult> findByResultFalseAndPatientInOrValueGreaterThanAndPatientInAndTimeCreatedAfterOrderByTimeCreatedDesc(List<Patient> patients, Integer value, List<Patient> patientList, Date date, Pageable pageable);
    @Query("select t from TestResult t where t.patient in ?1 and (t.value > ?2 or t.result = false) and t.timeCreated>t.patient.positiveDate order by t.timeCreated desc")
    Page<TestResult> getNegativeResult(List<Patient> patients,Float value,Pageable pageable);
    @Query("select t from TestResult t where t.patient in ?1 and t.result = false and t.timeCreated>t.patient.positiveDate order by t.timeCreated desc")
    Page<TestResult> getNegativeResult(List<Patient> patient,Pageable pageable);
    List<TestResult> findByPatient_User_UsernameAndResultTrue(String username);
    List<TestResult> findByPatient_User_Username(String username);
}
