package com.lvtn.module.nurse.service;

import com.lvtn.module.shared.dto.*;
import com.lvtn.module.shared.model.PatientStatus;
import com.lvtn.platform.common.PageResponse;

import java.util.List;

public interface NurseService {
    PageResponse<PatientInfoDto> getPatientList(PatientListRequestDto patientListRequestDto, String username);
    PageResponse<RequirementDto> getRequirementList(RequirementListRequestDto requirementListRequestDto, String username);
    PatientInfoDto changePatientStatus(PatientStatusDto patientStatusDto, String patientCmnd);
    PageResponse<PrescriptionResponseDto> getPrescription(PrescriptionListRequestDto prescriptionListRequestDto, String username);
    PrescriptionDto getMedicineBatchHistory(Long id);
    void checkPrescription(boolean check,Long id);
}
