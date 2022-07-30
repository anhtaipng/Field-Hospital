package com.lvtn.module.shared.dto;

import com.lvtn.module.shared.enumeration.PatientStatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Data
public class PatientStatusDto {

    private PatientStatusType patientStatusType;
    private String covidHospital;
}
