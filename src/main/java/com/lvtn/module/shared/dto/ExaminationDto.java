package com.lvtn.module.shared.dto;


import com.lvtn.module.shared.enumeration.GroupType;
import com.lvtn.module.shared.model.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ExaminationDto {
    private Date examinationTime;

    private String diagnostic;

    private String note;

    private List<Symptom> symptoms;

    private String doctorName;

    private String patientName;

    private String doctor;

    private String patient;

    private GroupType groupType;

    private List<PrescriptionDetailDto> prescriptionDetail;

    private List<RequirementDto> requirements;
}
