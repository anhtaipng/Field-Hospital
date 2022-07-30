package com.lvtn.module.shared.dto;


import lombok.Data;

import java.util.List;

@Data
public class PatientContactResponseDto {
    private List<EmployeeDto> doctor;
    private List<EmployeeDto> nurse;
    private List<DependentDto> dependent;
}
