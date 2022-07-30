package com.lvtn.module.shared.service;

import com.lvtn.module.shared.dto.EmployeeDto;
import com.lvtn.module.shared.dto.NumberOfPatientsDto;
import com.lvtn.module.shared.dto.PatientInfoDto;

import java.util.List;

public interface EmployeeCommonService {
    EmployeeDto getEmployeeInfo(String username);
    EmployeeDto updateEmployeeInfo(EmployeeDto employeeDto,String username);
    List<NumberOfPatientsDto> countNumberOfPatient(String username, String roleName);
}
