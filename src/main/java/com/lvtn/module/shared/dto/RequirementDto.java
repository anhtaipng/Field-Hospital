package com.lvtn.module.shared.dto;

import com.lvtn.module.shared.enumeration.GroupType;
import com.lvtn.module.shared.enumeration.PatientStatusType;
import com.lvtn.module.shared.enumeration.RequirementStatus;
import com.lvtn.module.shared.enumeration.RequirementType;
import com.lvtn.module.shared.model.*;
import lombok.Data;

import java.util.Date;

@Data
public class RequirementDto {

    private RequirementStatus status;

    private Date executionTime;

    private RequirementType requirementType;

    private String statisticType;

    private Date createTime;

    private String testType;

    private PatientStatusType patientStatusType;

    private String covidHospital;

    private PatientLocationDto patientLocation;

    private String patientCmnd;

    private String patientName;

    private GroupType groupType;

    private boolean isAuto;
}
