package com.lvtn.module.admin.service;


import com.lvtn.module.admin.dto.*;
import com.lvtn.module.shared.dto.*;
import com.lvtn.module.shared.enumeration.GroupType;
import com.lvtn.module.shared.model.*;
import com.lvtn.platform.common.PageResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminService {
    EmployeeDto signupAdmin(UserSignupDto userDto);
    EmployeeDto signupDoctor(UserSignupDto userDto);
    EmployeeDto signupNurse(UserSignupDto userDto);

    List<MedicineBatchDto> getMedicineBatchList(String medicineName);
    MedicineBatchDto addMedicineBatch(MedicineBatchDto medicineBatchDto);
    MedicineBatchDto updateMedicineBatch(MedicineBatchDto medicineBatchDto);
    void deleteMedicineBatch(MedicineBatchDeleteRequestDto medicineBatchDeleteRequestDto);

    Symptom addSymptom(Symptom symptom);
    Symptom updateSymptom(Symptom symptom);
    List<Symptom> getSymptom();
    void deleteSymptom(Symptom symptom);

    PageResponse<BvdcGroupResponseDto> getBvdcGroupList(BvdcGroupListRequestDto bvdcGroupListRequestDto);
    BvdcGroupResponseDto addBvdcGroup(BvdcGroupDto bvdcGroupDto);
    BvdcGroupResponseDto updateBvdcGroup(BvdcGroupDto bvdcGroupDto);
    void deleteBvdcGroup(BvdcGroupDto bvdcGroupDto);
    List<BvdcGroupResponseDto> getListBvdcGroup();
    void transferPatient(Long id1, Long id2);

    PageResponse<PatientInfoDto> getPatientList(PatientListRequestDto patientListRequestDto, String username);
    PatientDashboardDto getPatientDashboard();

    StatisticType createStatsType(StatisticType statisticType);
    StatisticType updateStatsType(StatisticType statisticType);
    void deleteStatsType(StatisticType statisticType);
    List<StatisticType> getStatsTypeList();

    PageResponse<DoctorDto> getDoctorList(DoctorListRequestDto doctorListRequestDto);
    List<DoctorDto> getListDoctorNonGroup(DoctorNonGroupRequestDto doctorNonGroupRequestDto);

    PageResponse<NurseDto> getNurseList(NurseListRequestDto nurseListRequestDto);
    List<NurseDto> getListNurseNonGroup(NurseNonGroupRequestDto nurseNonGroupRequestDto);

    MedicalRecordDto getMedicalRecord(String username);

    SickbedDashboardDto getSickbedDashboard();

    List<CountTestInAWeekDto> getCountTestInAWeek();
}
