package com.lvtn.module.admin.dto;

import com.lvtn.module.shared.dto.*;
import com.lvtn.module.shared.enumeration.Gender;
import com.lvtn.module.shared.enumeration.PatientStatusType;
import com.lvtn.module.shared.model.PrescriptionDetail;
import com.lvtn.module.shared.model.StatisticResult;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MedicalRecordDto {
    //PatientInfo
    private String name;
    private Gender gender;
    private Date birthDate;
    private String bhyt;
    private String tinh;
    private String huyen;
    private String xa;
    private String thon;
    private String cmnd;
    private PatientStatusType status;
    private String avatarURL;
    private String email;
    private String phone;
    private Date hospitalizedDate;
    private Date dischargeDate;

    //Patient
    private String toCovidHospital;

    //
    List<DependentDto> dependents;

    List<AllergyDto> allergys;

    List<SicknessDto> sicknesses;

    List<SurgeryDto> surgerys;

    List<VaccineDto> vaccines;

    List<ExaminationDto> examinations;

    List<PrescriptionDetail> prescriptions;

    List<TestResultDto> tests;

    List<StatisticResult> statsResults;


}
