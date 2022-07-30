package com.lvtn.module.shared.service.implementation;

import com.lvtn.module.patient.common.ApiPatientMesssage;
import com.lvtn.module.shared.dto.EmployeeDto;
import com.lvtn.module.shared.dto.NumberOfPatientsDto;
import com.lvtn.module.shared.mapper.PatientMapper;
import com.lvtn.module.shared.model.*;
import com.lvtn.module.shared.repository.*;
import com.lvtn.module.shared.service.EmployeeCommonService;
import com.lvtn.platform.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class EmployeeCommonServiceImpl implements EmployeeCommonService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PatientMapper patientMapper;
    @Autowired
    BvdcGroupRepository bvdcGroupRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    NurseRepository nurseRepository;
    @Autowired
    PatientRepository patientRepository;

    @Override
    public EmployeeDto getEmployeeInfo(String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new ApiException(ApiPatientMesssage.NOT_FOUND);
        }
        return patientMapper.mapUserToEmployeeDto(userRepository.findUserByUsername(username));
    }

    @Override
    public EmployeeDto updateEmployeeInfo(EmployeeDto employeeDto, String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new ApiException(ApiPatientMesssage.NOT_FOUND);
        }
        if (employeeDto.getAvatarURL()!=null) {
            user.setAvatarURL(employeeDto.getAvatarURL());
        }
        if (employeeDto.getEmail()!=null) {
            user.setEmail(employeeDto.getEmail());
        }
        if (employeeDto.getBirthDate()!=null) {
            user.setBirthDate(employeeDto.getBirthDate());
        }
        if (employeeDto.getGender()!=null) {
            user.setGender(employeeDto.getGender());
        }
        if (employeeDto.getName()!=null) {
            user.setName(employeeDto.getName());
        }
        if (employeeDto.getPhone()!=null) {
            user.setPhone(employeeDto.getPhone());
        }
        return patientMapper.mapUserToEmployeeDto(userRepository.save(user));
    }

    @Override
    public List<NumberOfPatientsDto> countNumberOfPatient(String username, String roleName) {
        List<BvdcGroup> bvdcGroupList;
        List<NumberOfPatientsDto> noPatients = new ArrayList<>();
        if (roleName.equals("ROLE_DOCTOR")) {
            Doctor doctor = doctorRepository.findDoctorByUser_Username(username);
            bvdcGroupList = bvdcGroupRepository.findByDoctors(doctor);
        } else {
            Nurse nurse = nurseRepository.findNurseByUser_Username(username);
            bvdcGroupList = bvdcGroupRepository.findByNurses(nurse);
        }
        if (bvdcGroupList != null && bvdcGroupList.size()!=0) {
            for (BvdcGroup bvdcGroup : bvdcGroupList) {
                noPatients.add(NumberOfPatientsDto.builder().count(patientRepository.countByBvdcGroup(bvdcGroup)).groupType(bvdcGroup.getGroupType()).build());
            }
            return noPatients;
        }
        return null;
    }
}
