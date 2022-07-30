package com.lvtn.module.shared.mapper;

import com.lvtn.module.shared.dto.*;
import com.lvtn.module.shared.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PatientMapper {

    public PatientInfoDto mapPatientToPatientInfoResponseDto(Patient patient) {
        PatientInfoDto patientInfoDto = new PatientInfoDto();
        patientInfoDto.setName(patient.getUser().getName());
        patientInfoDto.setCmnd(patient.getUser().getUsername());
        patientInfoDto.setBhyt(patient.getBhyt());
        patientInfoDto.setEmail(patient.getUser().getEmail());
        patientInfoDto.setBirthDate(patient.getUser().getBirthDate());
        patientInfoDto.setGender(patient.getUser().getGender());
        patientInfoDto.setTinh(patient.getTinh());
        patientInfoDto.setHuyen(patient.getHuyen());
        patientInfoDto.setXa(patient.getXa());
        patientInfoDto.setThon(patient.getThon());
        patientInfoDto.setPhone(patient.getUser().getPhone());
        patientInfoDto.setIsAutoGroup(patient.getIsAutoGroup());
        patientInfoDto.setHospitalizedDate(patient.getHospitalizedDate());
        patientInfoDto.setDischargeDate(patient.getDischargeDate());
        patientInfoDto.setStatus(patient.getCurrentPatientStatus());
        patientInfoDto.setAvatarURL(patient.getUser().getAvatarURL());
        if (patient.getBvdcGroup()!=null) {
            patientInfoDto.setGroupType(patient.getBvdcGroup().getGroupType());
                }
        return patientInfoDto;
    }


    public PatientContactResponseDto mapPatientToPatientContactResponseDto(Patient patient) {
        PatientContactResponseDto patientContactResponseDto = new PatientContactResponseDto();
        if (patient.getBvdcGroup()!=null) {
            patientContactResponseDto.setDoctor(patient.getBvdcGroup().getDoctors().stream().map(this::mapEmployeeToEmployeeResponseDto).collect(Collectors.toList()));
            patientContactResponseDto.setNurse(patient.getBvdcGroup().getNurses().stream().map(this::mapEmployeeToEmployeeResponseDto).collect(Collectors.toList()));
        }
        if (patient.getDependents()!=null && patient.getDependents().size()>0){
            patientContactResponseDto.setDependent(patient.getDependents().stream().map(this::mapDependentToDependentResponseDto).collect(Collectors.toList()));
        }
        return patientContactResponseDto;
    }


    public EmployeeDto mapEmployeeToEmployeeResponseDto(Doctor doctor) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setName(doctor.getUser().getName());
        employeeDto.setCmnd(doctor.getUser().getUsername());
        employeeDto.setEmail(doctor.getUser().getEmail());
        employeeDto.setBirthDate(doctor.getUser().getBirthDate());
        employeeDto.setGender(doctor.getUser().getGender());
        employeeDto.setPhone(doctor.getUser().getPhone());
        employeeDto.setShift(doctor.getShift());
        employeeDto.setAvatarURL(doctor.getUser().getAvatarURL());
        employeeDto.setRoleName(doctor.getUser().getAuthorities().stream().collect(Collectors.toList()).get(0).getAuthority());
        return employeeDto;
    }

    public User mapEmployeeDtoToUser(EmployeeDto employeeDto) {
        User user = new User();
        BeanUtils.copyProperties(employeeDto,user);
        return user;
    }

    public EmployeeDto mapEmployeeToEmployeeResponseDto(Nurse nurse) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setName(nurse.getUser().getName());
        employeeDto.setCmnd(nurse.getUser().getUsername());
        employeeDto.setEmail(nurse.getUser().getEmail());
        employeeDto.setBirthDate(nurse.getUser().getBirthDate());
        employeeDto.setGender(nurse.getUser().getGender());
        employeeDto.setPhone(nurse.getUser().getPhone());
        employeeDto.setShift(nurse.getShift());
        employeeDto.setAvatarURL(nurse.getUser().getAvatarURL());
        employeeDto.setRoleName(nurse.getUser().getAuthorities().stream().collect(Collectors.toList()).get(0).getAuthority());
        return employeeDto;
    }

    public EmployeeDto mapEmployeeToEmployeeResponseDto(Admin admin) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setName(admin.getUser().getName());
        employeeDto.setCmnd(admin.getUser().getUsername());
        employeeDto.setEmail(admin.getUser().getEmail());
        employeeDto.setBirthDate(admin.getUser().getBirthDate());
        employeeDto.setGender(admin.getUser().getGender());
        employeeDto.setPhone(admin.getUser().getPhone());
        employeeDto.setAvatarURL(admin.getUser().getAvatarURL());
        return employeeDto;
    }

    public EmployeeDto mapUserToEmployeeDto(User user) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setName(user.getName());
        employeeDto.setCmnd(user.getUsername());
        employeeDto.setEmail(user.getEmail());
        employeeDto.setBirthDate(user.getBirthDate());
        employeeDto.setGender(user.getGender());
        employeeDto.setPhone(user.getPhone());
        employeeDto.setAvatarURL(user.getAvatarURL());
        employeeDto.setRoleName(user.getAuthorities().stream().collect(Collectors.toList()).get(0).getAuthority());
        return employeeDto;
    }

    public DependentDto mapDependentToDependentResponseDto(Dependent dependent) {
        DependentDto dependentResponseDto = new DependentDto();
        dependentResponseDto.setName(dependent.getName());
        dependentResponseDto.setEmail(dependent.getEmail());
        dependentResponseDto.setRelationShip(dependent.getRelationShip());
        dependentResponseDto.setPhone(dependent.getPhone());
        return dependentResponseDto;
    }

    public AllergyDto mapAllergyToAllergyDto(Allergy allergy){
        AllergyDto allergyDto = new AllergyDto();
        BeanUtils.copyProperties(allergy, allergyDto);
        return allergyDto;
    }

    public SicknessDto mapSicknessToSicknessDto(Sickness sickness){
        SicknessDto sicknessDto = new SicknessDto();
        BeanUtils.copyProperties(sickness, sicknessDto);
        return sicknessDto;
    }

    public SurgeryDto mapSurgeryToSurgeryDto(Surgery surgery){
        SurgeryDto surgeryDto = new SurgeryDto();
        BeanUtils.copyProperties(surgery, surgeryDto);
        return surgeryDto;
    }

    public VaccineDto mapVaccineToVaccineDto(Vaccine vaccine){
        VaccineDto vaccineDto = new VaccineDto();
        BeanUtils.copyProperties(vaccine, vaccineDto);
        return vaccineDto;
    }

    public Allergy mapAllergyDtoToAllergy(AllergyDto allergyDto, Patient patient){
        Allergy allergy = new Allergy();
        BeanUtils.copyProperties(allergyDto, allergy);
        allergy.setPatient(patient);
        return allergy;
    }

    public Sickness mapSicknessDtoDtoToSickness(SicknessDto sicknessDto, Patient patient){
        Sickness sickness = new Sickness();
        BeanUtils.copyProperties(sicknessDto, sickness);
        sickness.setPatient(patient);
        return sickness;
    }
    public Surgery mapSurgeryDtoToSurgery(SurgeryDto surgeryDto, Patient patient){
        Surgery surgery = new Surgery();
        BeanUtils.copyProperties(surgeryDto, surgery);
        surgery.setPatient(patient);
        return surgery;
    }
    public Vaccine mapVaccineDtoToVaccine(VaccineDto vaccineDto, Patient patient){
        Vaccine vaccine = new Vaccine();
        BeanUtils.copyProperties(vaccineDto, vaccine);
        vaccine.setPatient(patient);
        return vaccine;
    }

    public TestResultDto mapTestResultToTestResultDto(TestResult testResult){
        TestResultDto testResultDto = new TestResultDto();
        BeanUtils.copyProperties(testResult, testResultDto);
        testResultDto.setTestType(testResult.getTestType().getTestType());
        testResultDto.setPatientCmnd(testResult.getPatient().getUser().getUsername());
        testResultDto.setPatientName(testResult.getPatient().getUser().getName());
        return testResultDto;
    }

    public TestResult mapTestResultDtoToTestResult(TestResultDto testResultDto, Patient patient, TestType testType){
        TestResult testResult = new TestResult();
        BeanUtils.copyProperties(testResultDto, testResult);
        testResult.setPatient(patient);
        testResult.setTestType(testType);
        return testResult;
    }


}
