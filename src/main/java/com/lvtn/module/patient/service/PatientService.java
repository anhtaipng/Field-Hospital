package com.lvtn.module.patient.service;

import com.lvtn.module.patient.dto.PatientSignupDto;
import com.lvtn.module.shared.dto.*;
import com.lvtn.platform.common.PageResponse;

public interface PatientService {
    PatientInfoDto signupPatient(PatientSignupDto patientSignupDto);
    PageResponse<RequirementDto> getRequirementList(RequirementListRequestDto requirementListRequestDto, String username);
}
