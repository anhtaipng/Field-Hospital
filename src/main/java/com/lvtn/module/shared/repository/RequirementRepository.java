package com.lvtn.module.shared.repository;

import com.lvtn.module.shared.enumeration.PatientStatusType;
import com.lvtn.module.shared.enumeration.RequirementStatus;
import com.lvtn.module.shared.enumeration.RequirementType;
import com.lvtn.module.shared.model.BvdcGroup;
import com.lvtn.module.shared.model.Medicine;
import com.lvtn.module.shared.model.Patient;
import com.lvtn.module.shared.model.Requirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;

import java.util.Date;
import java.util.List;

public interface RequirementRepository extends JpaRepository<Requirement,Long> {
    Page<Requirement> findAllByStatusAndRequirementTypeAndPatientInOrderByRequirementTypeDescCreateTimeDesc(RequirementStatus status, RequirementType requirementType,List<Patient> patients, Pageable pageable);
    Page<Requirement> findAllByStatusAndPatientInOrderByRequirementTypeAscCreateTimeDesc(RequirementStatus status, List<Patient> patients, Pageable pageable);
    Page<Requirement> findAllByRequirementTypeAndPatientInOrderByRequirementTypeAscCreateTimeDesc(RequirementType requirementType, List<Patient> patients, Pageable pageable);
    Page<Requirement> findAllByPatientInOrderByRequirementTypeAscCreateTimeDesc(List<Patient> patients,Pageable pageable);
    Page<Requirement> findAllByStatusAndRequirementTypeAndPatientInOrderByRequirementTypeAscCreateTimeAsc(RequirementStatus status, RequirementType requirementType,List<Patient> patients, Pageable pageable);
    Page<Requirement> findAllByStatusAndRequirementTypeInAndPatientInOrderByRequirementTypeAscCreateTimeAsc(RequirementStatus status, List<RequirementType> requirementTypeList,List<Patient> patients, Pageable pageable);
    Page<Requirement> findByStatusAndRequirementTypeAndPatient_User_UsernameOrderByCreateTimeAsc(RequirementStatus status, RequirementType requirementType, String username, Pageable pageable);
    Page<Requirement> findByStatusAndPatient_User_UsernameOrderByRequirementTypeAscCreateTimeAsc(RequirementStatus status, String username, Pageable pageable);
    Page<Requirement> findByStatusAndRequirementTypeInAndPatient_User_UsernameOrderByRequirementTypeAscCreateTimeAsc(RequirementStatus status, List<RequirementType> requirementTypeList, String username, Pageable pageable);
    List<Requirement> findByStatusAndRequirementTypeAndPatient_User_UsernameOrderByCreateTimeDesc(RequirementStatus status, RequirementType requirementType, String username);
    List<Requirement> findAllByStatusAndRequirementTypeAndPatientIn(RequirementStatus requirementStatus, RequirementType requirementType, List<Patient> patients);
    List<Requirement> findByStatusAndPatient_User_Username(RequirementStatus requirementStatus, String username);
    Long countByStatusAndRequirementTypeAndPatientStatus_PatientStatusType(RequirementStatus status, RequirementType requirementType, PatientStatusType patientStatusType);
    List<Requirement> findByStatusAndRequirementTypeAndTestType_TestType(RequirementStatus status, RequirementType requirementType, String testType);
    List<Requirement> findByStatusAndRequirementTypeAndPatient_User_UsernameAndCreateTimeBefore(RequirementStatus requirementStatus, RequirementType requirementType, String username, Date createTime);
}
