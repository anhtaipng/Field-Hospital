package com.lvtn.module.shared.service;

import com.lvtn.module.shared.dto.*;
import com.lvtn.module.shared.model.Patient;
import com.lvtn.module.shared.model.Prescription;
import com.lvtn.platform.common.PageResponse;

import java.util.List;

public interface PatientCommonService {
    PatientInfoDto getPatientInfo(String patient_cmnd);
    PatientInfoDto updatePatientInfo(PatientInfoDto patientInfoDto,String patient_cmnd);
    PatientLocationDto updateCurrentLocation(PatientLocationDto patientLocationDto,String patient_cmnd);
    PatientLocationDto getPatientCurrentLocation(String patient_cmnd);
    PatientContactResponseDto getPatientContact(String patient_cmnd);
    DependentDto addPatientDependent(DependentDto dependentDto, String patient_cmnd);
    void deletePatientDependent(DependentDto dependentDto, String patient_cmnd);
    DependentDto updatePatientDependent(DependentDto dependentDto, String patient_cmnd);
    PrescriptionResponseDto getCurrentPrescription(String patient_cmnd);
    PageResponse<ExaminationDto> getExamination(ExaminationListRequestDto examinationListRequestDto, String patient_cmnd);
    List<AllergyDto> getAllergy(String username);
    List<SicknessDto> getSickness(String username);
    List<SurgeryDto> getSurgery(String username);
    List<VaccineDto> getVaccine(String username);
    AllergyDto addAllergy(AllergyDto allergyDto, String username);
    AllergyDto updateAllergy(AllergyDto allergyDto, String username);
    AllergyDto deleteAllergy(Long idAllergy);
    SicknessDto addSickness(SicknessDto sicknessDto, String username);
    SicknessDto updateSickness(SicknessDto sicknessDto, String username);
    SicknessDto deleteSickness(Long idSickness);
    SurgeryDto addSurgery(SurgeryDto surgeryDto, String username);
    SurgeryDto updateSurgery(SurgeryDto surgeryDto, String username);
    SurgeryDto deleteSurgery(Long idSurgery);
    VaccineDto addVaccine(VaccineDto vaccineDto, String username);
    VaccineDto updateVaccine(VaccineDto vaccineDto, String username);
    VaccineDto deleteVaccine(Long idVaccine);

    PageResponse<TestResultDto> getTestResult(TestResultRequest testResultRequest,String username);
    TestResultDto addTestResult(TestResultDto testResultDto, String username);
    TestResultDto updateTestResult(TestResultDto testResultDto, String username);
    TestResultDto deleteTestResult(Long idTestResult);

    RequirementDto addRequirement(RequirementDto requirementDto,String username);
    PrescriptionDetailDto addPrescriptionDetail(PrescriptionDetailDto prescriptionDetailDto, Prescription prescription);
    void checkRequirement(RequirementDto requirementDto, Patient patient);

    void support(String supportUsername,String username);


}
