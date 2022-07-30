package com.lvtn.module.doctor.service;

import com.lvtn.module.shared.dto.*;
import com.lvtn.module.shared.enumeration.GroupType;
import com.lvtn.module.shared.model.Doctor;
import com.lvtn.module.shared.model.Patient;
import com.lvtn.platform.common.PageResponse;

public interface DoctorService {
    ExaminationDto createExamination(ExaminationDto examinationDto, String username);

    PageResponse<PatientInfoDto> getPatientList(PatientListRequestDto patientListRequestDto, String username);

    PageResponse<ExaminationDto> getExaminationList(ExaminationListRequestDto examinationListRequestDto, String username);

    MedicineRequirementDto saveMedicineRequirement(MedicineRequirementDto medicineRequirementDto, String username);

    PageResponse<MedicineRequirementDto> getMedicineRequirementList(RequirementListRequestDto requirementListRequestDto, String username);

    PageResponse<RequirementDto> getRequirementList(RequirementListRequestDto requirementListRequestDto, String username);

    PageResponse<TestResultDto> getNegativePatientList(ListRequestDto listRequestDto,String username);

    PatientInfoDto changeGroupType(GroupType newGroupType, String patientCmnd, String username);

    RequirementDto requestChangePatientStatus(PatientStatusDto patientStatusDto, String patientCmnd, String username);

    void cancelXuatVien(String patientCmnd);
}