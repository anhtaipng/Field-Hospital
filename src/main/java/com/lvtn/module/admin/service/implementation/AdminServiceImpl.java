package com.lvtn.module.admin.service.implementation;

import com.google.common.base.Strings;
import com.lvtn.module.admin.common.ApiAdminMesssage;
import com.lvtn.module.admin.dto.*;
import com.lvtn.module.admin.mapper.AdminMapper;
import com.lvtn.module.admin.service.AdminService;
import com.lvtn.module.patient.common.ApiPatientMesssage;
import com.lvtn.module.shared.common.ApiSharedMesssage;
import com.lvtn.module.shared.dto.*;
import com.lvtn.module.shared.enumeration.*;
import com.lvtn.module.shared.mapper.CommonMapper;
import com.lvtn.module.shared.mapper.MedicineMapper;
import com.lvtn.module.shared.mapper.PatientMapper;
import com.lvtn.module.shared.model.*;
import com.lvtn.module.shared.repository.*;
import com.lvtn.module.shared.service.PatientCommonService;
import com.lvtn.module.user.service.UserService;
import com.lvtn.platform.common.PageResponse;
import com.lvtn.platform.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class AdminServiceImpl implements AdminService {

    @Autowired
    UserService userService;

    @Autowired
    PatientCommonService patientCommonService;

    @Autowired
    PatientMapper patientMapper;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    NurseRepository nurseRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    MedicineRepository medicineRepository;

    @Autowired
    MedicineBatchRepository medicineBatchRepository;

    @Autowired
    SymptomRepository symptomRepository;

    @Autowired
    BvdcGroupRepository bvdcGroupRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    StatisticTypeRepository statisticTypeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PrescriptionRepository prescriptionRepository;

    @Autowired
    PrescriptionDetailRepository prescriptionDetailRepository;

    @Autowired
    TestResultRepository testResultRepository;

    @Autowired
    ExaminationRepository examinationRepository;

    @Autowired
    StatisticResultRepository statisticResultRepository;

    @Autowired
    SickbedRepository sickbedRepository;

    @Autowired
    RequirementRepository requirementRepository;

    @Autowired
    TestTypeRepository testTypeRepository;

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    MedicineMapper medicineMapper;

    @Autowired
    CommonMapper commonMapper;

    @Override
    public EmployeeDto signupAdmin(UserSignupDto userDto) {
        User user = userService.signupUser(userDto, "ROLE_ADMIN");
        Admin admin = Admin.builder().user(user).build();
        return patientMapper.mapEmployeeToEmployeeResponseDto(adminRepository.save(admin));
    }

    @Override
    public List<MedicineBatchDto> getMedicineBatchList(String medicineName) {
        if (medicineRepository.findById(medicineName).isEmpty()) {
            throw new ApiException(ApiAdminMesssage.MEDICINE_NOT_FOUND);
        }
        List<MedicineBatch> medicineBatches = medicineBatchRepository.findByMedicineName_medicineName(medicineName);
        return medicineBatches.stream().map(adminMapper::mapMedicineBatchToMedicineBatchDto).collect(Collectors.toList());
    }

    @Override
    public MedicineBatchDto addMedicineBatch(MedicineBatchDto medicineBatchDto) {
        if (medicineRepository.findById(medicineBatchDto.getMedicineName()).isEmpty()) {
            throw new ApiException(ApiAdminMesssage.MEDICINE_NOT_FOUND);
        }
        if (medicineBatchRepository.findByBatchNumber(medicineBatchDto.getBatchNumber()) != null) {
            throw new ApiException(ApiAdminMesssage.MEDICINE_BATCH_EXISTED);
        }
        return adminMapper.mapMedicineBatchToMedicineBatchDto(medicineBatchRepository.save(adminMapper.mapMedicineBatchDtoToMedicineBatch(medicineBatchDto)));
    }

    @Override
    public MedicineBatchDto updateMedicineBatch(MedicineBatchDto medicineBatchDto) {
        if (medicineBatchRepository.findById(medicineBatchDto.getId()).isEmpty()) {
            throw new ApiException(ApiAdminMesssage.MEDICINE_BATCH_NOT_FOUND);
        }
        if (medicineRepository.findById(medicineBatchDto.getMedicineName()).isEmpty()) {
            throw new ApiException(ApiAdminMesssage.MEDICINE_NOT_FOUND);
        }
        return adminMapper.mapMedicineBatchToMedicineBatchDto(medicineBatchRepository.save(adminMapper.mapMedicineBatchDtoToMedicineBatch(medicineBatchDto)));
    }

    @Override
    public void deleteMedicineBatch(MedicineBatchDeleteRequestDto medicineBatchDeleteRequestDto) {
        if (medicineBatchRepository.findByBatchNumber(medicineBatchDeleteRequestDto.getBatchNumber()) == null) {
            throw new ApiException(ApiAdminMesssage.MEDICINE_BATCH_NOT_FOUND);
        }
        MedicineBatch medicineBatch = medicineBatchRepository.findByBatchNumber(medicineBatchDeleteRequestDto.getBatchNumber());
        medicineBatchRepository.delete(medicineBatch);
    }

    @Override
    public Symptom addSymptom(Symptom symptom) {
        if (symptomRepository.findById(symptom.getName()).isPresent()) {
            throw new ApiException(ApiAdminMesssage.SYMPTOM_EXISTED);
        }
        return symptomRepository.save(symptom);
    }

    @Override
    public Symptom updateSymptom(Symptom symptom) {
        if (symptomRepository.findById(symptom.getName()).isEmpty()) {
            throw new ApiException(ApiAdminMesssage.SYMPTOM_NOT_FOUND);
        }
        return symptomRepository.save(symptom);
    }

    @Override
    public List<Symptom> getSymptom() {
        return symptomRepository.findAll();
    }

    @Override
    public void deleteSymptom(Symptom symptom) {
        if (symptomRepository.findById(symptom.getName()).isEmpty()) {
            throw new ApiException(ApiAdminMesssage.SYMPTOM_NOT_FOUND);
        }
        symptomRepository.delete(symptom);
    }

    @Override
    public PageResponse<PatientInfoDto> getPatientList(PatientListRequestDto patientListRequestDto, String username) {
        Page<Patient> patients;
        Pageable pageable = PageRequest.of(patientListRequestDto.getPageNum(), patientListRequestDto.getPageSize());

        if (Strings.isNullOrEmpty(patientListRequestDto.getKeyword())) {
            patients = patientRepository.findAll(pageable);
        } else {
            if (patientListRequestDto.getKeyword().charAt(0) >= '0' && patientListRequestDto.getKeyword().charAt(0) <= '9') {
                patients = patientRepository.findByUser_UsernameContaining(patientListRequestDto.getKeyword(), pageable);
            } else {
                patients = patientRepository.findByUser_NameContaining(patientListRequestDto.getKeyword(), pageable);
            }
        }
        return PageResponse.buildPageResponse(patients.map(patientMapper::mapPatientToPatientInfoResponseDto));
    }

    @Override
    public PatientDashboardDto getPatientDashboard() {
        PatientDashboardDto patientDashboardDto = new PatientDashboardDto();
        List<Patient> patients = patientRepository.findAll();
        patientDashboardDto.setNamvien(patients.stream().filter(x -> x.getPatientStatus().getPatientStatusType().equals(PatientStatusType.NAM_VIEN)).count());
        patientDashboardDto.setChuyenvien(patients.stream().filter(x -> x.getPatientStatus().getPatientStatusType().equals(PatientStatusType.CHUYEN_VIEN)).count());
        patientDashboardDto.setXuatvien(patients.stream().filter(x -> x.getPatientStatus().getPatientStatusType().equals(PatientStatusType.XUAT_VIEN)).count());
        patientDashboardDto.setTuvong(patients.stream().filter(x -> x.getPatientStatus().getPatientStatusType().equals(PatientStatusType.TU_VONG)).count());
        return patientDashboardDto;
    }

    @Override
    public StatisticType createStatsType(StatisticType statisticType) {
        Optional<StatisticType> statisticType1 = statisticTypeRepository.findById(statisticType.getStatisticType());
        if (statisticType1.isPresent()) {
            throw new ApiException(ApiSharedMesssage.STATISTIC_TYPE_EXIST);
        }
        return statisticTypeRepository.save(statisticType);
    }

    @Override
    public StatisticType updateStatsType(StatisticType statisticType) {
        Optional<StatisticType> statisticType1 = statisticTypeRepository.findById(statisticType.getStatisticType());
        if (statisticType1.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.STATISTIC_TYPE_NOT_FOUND);
        }
        return statisticTypeRepository.save(statisticType);
    }

    @Override
    public void deleteStatsType(StatisticType statisticType) {
        Optional<StatisticType> statisticType1 = statisticTypeRepository.findById(statisticType.getStatisticType());
        if (statisticType1.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.STATISTIC_TYPE_NOT_FOUND);
        }
        statisticTypeRepository.delete(statisticType);
    }

    @Override
    public List<StatisticType> getStatsTypeList() {
        return statisticTypeRepository.findAll();
    }

    @Override
    public PageResponse<DoctorDto> getDoctorList(DoctorListRequestDto doctorListRequestDto) {
        Page<Doctor> doctors;
        Pageable pageable = PageRequest.of(doctorListRequestDto.getPageNum(), doctorListRequestDto.getPageSize());

        if (Strings.isNullOrEmpty(doctorListRequestDto.getKeyword())) {
            doctors = doctorRepository.findAll(pageable);
        } else {
            if (doctorListRequestDto.getKeyword().charAt(0) >= '0' && doctorListRequestDto.getKeyword().charAt(0) <= '9') {
                doctors = doctorRepository.findByUser_UsernameContaining(doctorListRequestDto.getKeyword(), pageable);
            } else {
                doctors = doctorRepository.findByUser_NameContaining(doctorListRequestDto.getKeyword(), pageable);
            }
        }
        return PageResponse.buildPageResponse(doctors.map(adminMapper::mapDoctorToDoctorDto));
    }

    @Override
    public PageResponse<NurseDto> getNurseList(NurseListRequestDto nurseListRequestDto) {
        Page<Nurse> nurses;
        Pageable pageable = PageRequest.of(nurseListRequestDto.getPageNum(), nurseListRequestDto.getPageSize());

        if (Strings.isNullOrEmpty(nurseListRequestDto.getKeyword())) {
            nurses = nurseRepository.findAll(pageable);
        } else {
            if (nurseListRequestDto.getKeyword().charAt(0) >= '0' && nurseListRequestDto.getKeyword().charAt(0) <= '9') {
                nurses = nurseRepository.findByUser_UsernameContaining(nurseListRequestDto.getKeyword(), pageable);
            } else {
                nurses = nurseRepository.findByUser_NameContaining(nurseListRequestDto.getKeyword(), pageable);
            }
        }
        return PageResponse.buildPageResponse(nurses.map(adminMapper::mapNurseToNurseDto));
    }

    @Override
    public EmployeeDto signupDoctor(UserSignupDto userDto) {
        User user = userService.signupUser(userDto, "ROLE_DOCTOR");
        Doctor doctor = Doctor.builder().user(user).build();
        doctor.setShift(userDto.getShift());
        return patientMapper.mapEmployeeToEmployeeResponseDto(doctorRepository.save(doctor));
    }

    @Override
    public List<DoctorDto> getListDoctorNonGroup(DoctorNonGroupRequestDto doctorNonGroupRequestDto) {
        List<Doctor> doctors = doctorRepository.findByShift(doctorNonGroupRequestDto.getShift());
        List<DoctorDto> doctorDtos = new ArrayList<>();
        for (Doctor doctor : doctors) {
            boolean check = true;
            if (!doctor.getBvdcGroups().isEmpty()) {
                for (BvdcGroup bvdcGroup : doctor.getBvdcGroups()) {
                    if (bvdcGroup.getGroupType() == doctorNonGroupRequestDto.getGroupType()) {
                        check = false;
                    }
                }
            }
            if (check) {
                doctorDtos.add(adminMapper.mapDoctorToDoctorDto(doctor));
            }
        }
        return doctorDtos;
    }

    @Override
    public EmployeeDto signupNurse(UserSignupDto userDto) {
        User user = userService.signupUser(userDto, "ROLE_NURSE");
        Nurse nurse = Nurse.builder().user(user).build();
        nurse.setShift(userDto.getShift());
        return patientMapper.mapEmployeeToEmployeeResponseDto(nurseRepository.save(nurse));
    }

    @Override
    public List<NurseDto> getListNurseNonGroup(NurseNonGroupRequestDto nurseNonGroupRequestDto) {
        List<Nurse> nurses = nurseRepository.findByShift(nurseNonGroupRequestDto.getShift());
        List<NurseDto> nurseDtos = new ArrayList<>();
        for (Nurse nurse : nurses) {
            boolean check = true;
            if (!nurse.getBvdcGroups().isEmpty()) {
                for (BvdcGroup bvdcGroup : nurse.getBvdcGroups()) {
                    if (bvdcGroup.getGroupType() == nurseNonGroupRequestDto.getGroupType()) {
                        check = false;
                    }
                }
            }
            if (check) {
                nurseDtos.add(adminMapper.mapNurseToNurseDto(nurse));
            }
        }
        return nurseDtos;
    }

    @Override
    public BvdcGroupResponseDto addBvdcGroup(BvdcGroupDto bvdcGroupDto) {
        for (String name : bvdcGroupDto.getNurses()) {
            if (nurseRepository.findNurseByUser_Username(name) == null) {
                throw new ApiException(ApiSharedMesssage.NURSE_NOT_FOUND);
            } else {
                Nurse nurse = nurseRepository.findNurseByUser_Username(name);
                for (BvdcGroup bvdcGroup : nurse.getBvdcGroups()) {
                    if (bvdcGroup.getGroupType() == bvdcGroupDto.getGroupType()) {
                        throw new ApiException(ApiSharedMesssage.NURSE_HAD_GROUP);
                    }
                }
            }
        }
        for (String name : bvdcGroupDto.getDoctors()) {
            if (doctorRepository.findDoctorByUser_Username(name) == null) {
                throw new ApiException(ApiSharedMesssage.DOCTOR_NOT_FOUND);
            } else {
                Doctor doctor = doctorRepository.findDoctorByUser_Username(name);
                for (BvdcGroup bvdcGroup : doctor.getBvdcGroups()) {
                    if (bvdcGroup.getGroupType() == bvdcGroupDto.getGroupType()) {
                        throw new ApiException(ApiSharedMesssage.DOCTOR_HAD_GROUP);
                    }
                }
            }
        }
        BvdcGroup bvdcGroup = adminMapper.mapBvdcGroupDtoToBvdcGroup(bvdcGroupDto);
        return adminMapper.mapBvdcGroupToBvdcGroupRespsonseDto(bvdcGroupRepository.save(bvdcGroup));
    }

    @Override
    public BvdcGroupResponseDto updateBvdcGroup(BvdcGroupDto bvdcGroupDto) {
        if (bvdcGroupRepository.findById(bvdcGroupDto.getId()).isEmpty()) {
            throw new ApiException(ApiAdminMesssage.BVDC_GROUP_NOT_FOUND);
        }

        for (String name : bvdcGroupDto.getNurses()) {
            if (nurseRepository.findNurseByUser_Username(name) == null) {
                throw new ApiException(ApiSharedMesssage.NURSE_NOT_FOUND);
            }
            else {
                Nurse nurse = nurseRepository.findNurseByUser_Username(name);
                for (BvdcGroup bvdcGroup : nurse.getBvdcGroups()) {
                    if (bvdcGroup.getGroupType() == bvdcGroupDto.getGroupType()) {
                        throw new ApiException(ApiSharedMesssage.NURSE_HAD_GROUP);
                    }
                }
            }
        }
        for (String name : bvdcGroupDto.getDoctors()) {
            if (doctorRepository.findDoctorByUser_Username(name) == null) {
                throw new ApiException(ApiSharedMesssage.DOCTOR_NOT_FOUND);
            }
            else {
                Doctor doctor = doctorRepository.findDoctorByUser_Username(name);
                for (BvdcGroup bvdcGroup : doctor.getBvdcGroups()) {
                    if (bvdcGroup.getGroupType() == bvdcGroupDto.getGroupType()) {
                        throw new ApiException(ApiSharedMesssage.DOCTOR_HAD_GROUP);
                    }
                }
            }
        }
        BvdcGroup bvdcGroup = adminMapper.mapBvdcGroupDtoToBvdcGroup(bvdcGroupDto);
        return adminMapper.mapBvdcGroupToBvdcGroupRespsonseDto(bvdcGroupRepository.save(bvdcGroup));
    }

    @Override
    public void deleteBvdcGroup(BvdcGroupDto bvdcGroupDto) {
        BvdcGroup bvdcGroup = bvdcGroupRepository.findById(bvdcGroupDto.getId()).get();
        if (bvdcGroupRepository.findById(bvdcGroupDto.getId()).isEmpty()) {
            throw new ApiException(ApiAdminMesssage.BVDC_GROUP_NOT_FOUND);
        }
        if (bvdcGroup.getPatient().size() > 0) {
            throw new ApiException(ApiAdminMesssage.BVDC_GROUP_HAS_PATIENT);
        }
        List<Doctor> doctors = new ArrayList<>();
        List<Nurse> nurses = new ArrayList<>();
        bvdcGroup.setDoctors(doctors);
        bvdcGroup.setNurses(nurses);
        bvdcGroupRepository.save(bvdcGroup);
        bvdcGroupRepository.delete(bvdcGroup);
    }

    @Override
    public List<BvdcGroupResponseDto> getListBvdcGroup() {
        return bvdcGroupRepository.findAll().stream().map(x -> adminMapper.mapBvdcGroupToBvdcGroupRespsonseDto(x)).collect(Collectors.toList());
    }

    @Override
    public void transferPatient(Long id1, Long id2) {
        BvdcGroup bvdcGroup1 = bvdcGroupRepository.findById(id1).get();
        BvdcGroup bvdcGroup2 = bvdcGroupRepository.findById(id2).get();
        if (bvdcGroupRepository.findById(id1).isEmpty()) {
            throw new ApiException(ApiAdminMesssage.BVDC_GROUP_NOT_FOUND);
        }
        if (bvdcGroupRepository.findById(id2).isEmpty()) {
            throw new ApiException(ApiAdminMesssage.BVDC_GROUP_NOT_FOUND);
        }
        for (Patient patient : bvdcGroup1.getPatient()) {
            patient.setBvdcGroup(bvdcGroup2);
            patientRepository.save(patient);
        }
    }

    @Override
    public PageResponse<BvdcGroupResponseDto> getBvdcGroupList(BvdcGroupListRequestDto bvdcGroupListRequestDto) {
        Page<BvdcGroup> bvdcGroups;
        Pageable pageable = PageRequest.of(bvdcGroupListRequestDto.getPageNum(), bvdcGroupListRequestDto.getPageSize());
        bvdcGroups = bvdcGroupRepository.findAll(pageable);
        return PageResponse.buildPageResponse(bvdcGroups.map(adminMapper::mapBvdcGroupToBvdcGroupRespsonseDto));
    }

    @Override
    public MedicalRecordDto getMedicalRecord(String username) {

        MedicalRecordDto medicalRecordDto = new MedicalRecordDto();
        User user = userRepository.findUserByUsername(username);
        Patient patient = patientRepository.findPatientByUser_Username(username);
        if (patient == null) {
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        List<AllergyDto> allergys = patientCommonService.getAllergy(username);
        List<SicknessDto> sicknesses = patientCommonService.getSickness(username);
        List<SurgeryDto> surgerys = patientCommonService.getSurgery(username);
        List<VaccineDto> vaccines = patientCommonService.getVaccine(username);
        List<ExaminationDto> examinations = examinationRepository.findByPatient_User_Username(username).stream()
                .map(x -> commonMapper.mapExaminationToExaminationResponseDto(x)).collect(Collectors.toList());
        List<TestResultDto> tests = testResultRepository.findByPatient_User_Username(username).stream().map(x -> patientMapper.mapTestResultToTestResultDto(x))
                .collect(Collectors.toList());
        List<StatisticResult> statisticResults = statisticResultRepository.findAllByPatient_User_Username(username);

        BeanUtils.copyProperties(user, medicalRecordDto);
        BeanUtils.copyProperties(patient, medicalRecordDto);
        medicalRecordDto.setStatus(patient.getCurrentPatientStatus());
        medicalRecordDto.setCmnd(username);
        medicalRecordDto.setDependents(patient.getDependents().stream().map(x -> patientMapper.mapDependentToDependentResponseDto(x)).collect(Collectors.toList()));
        medicalRecordDto.setAllergys(allergys);
        medicalRecordDto.setSicknesses(sicknesses);
        medicalRecordDto.setSurgerys(surgerys);
        medicalRecordDto.setVaccines(vaccines);
        medicalRecordDto.setExaminations(examinations);
        medicalRecordDto.setTests(tests);
        medicalRecordDto.setStatsResults(statisticResults);

        return medicalRecordDto;
    }

    @Override
    public SickbedDashboardDto getSickbedDashboard() {
        SickbedDashboardDto sickbedDashboardDto = new SickbedDashboardDto();
        List<Sickbed> sickbeds = sickbedRepository.findAll();
        sickbedDashboardDto.setEmpty(sickbeds.stream().filter(x -> x.getSickbedStatus().equals(SickbedStatus.EMPTY)).count());
        sickbedDashboardDto.setUsed(sickbeds.stream().filter(x -> x.getSickbedStatus().equals(SickbedStatus.USED)).count());
        sickbedDashboardDto.setRequested(sickbeds.stream().filter(x -> x.getSickbedStatus().equals(SickbedStatus.REQUESTED)).count());
        sickbedDashboardDto.setDisable(sickbeds.stream().filter(x -> x.getSickbedStatus().equals(SickbedStatus.DISABLE)).count());
        sickbedDashboardDto.setWaiting(requirementRepository.countByStatusAndRequirementTypeAndPatientStatus_PatientStatusType
                (RequirementStatus.DANG_THUC_HIEN, RequirementType.A_PATIENT_STATUS, PatientStatusType.XUAT_VIEN) +
                requirementRepository.countByStatusAndRequirementTypeAndPatientStatus_PatientStatusType
                        (RequirementStatus.DANG_THUC_HIEN, RequirementType.A_PATIENT_STATUS, PatientStatusType.CHUYEN_VIEN)
                +requirementRepository.countByStatusAndRequirementTypeAndPatientStatus_PatientStatusType
                        (RequirementStatus.DANG_THUC_HIEN, RequirementType.A_PATIENT_STATUS, PatientStatusType.TU_VONG));
        return sickbedDashboardDto;
    }

    @Override
    public List<CountTestInAWeekDto> getCountTestInAWeek() {
        List<CountTestInAWeekDto> countTestInAWeekDtoList = new ArrayList<>();
        Long nowTime = System.currentTimeMillis() / 1000;
        nowTime = nowTime - nowTime % 86400;
        List<TestType> testTypeList = testTypeRepository.findAll();
        for (TestType testType : testTypeList) {
            CountTestInAWeekDto countTestInAWeekDto = new CountTestInAWeekDto();
            countTestInAWeekDto.setTestTypes(testType.getTestType());
            countTestInAWeekDto.setDay0(0L);
            countTestInAWeekDto.setDay1(0L);
            countTestInAWeekDto.setDay2(0L);
            countTestInAWeekDto.setDay3(0L);
            countTestInAWeekDto.setDay4(0L);
            countTestInAWeekDto.setDay5(0L);
            countTestInAWeekDto.setDay6(0L);
            countTestInAWeekDto.setDay7(0L);
            List<Requirement> requirementList = requirementRepository.findByStatusAndRequirementTypeAndTestType_TestType(
                    RequirementStatus.DANG_THUC_HIEN, RequirementType.C_TEST, testType.getTestType());
            for (Requirement requirement : requirementList) {
                Long exeTime = requirement.getCreateTime().getTime() / 1000;
                if (exeTime >= nowTime && exeTime < nowTime + 86400) {
                    countTestInAWeekDto.setDay0(countTestInAWeekDto.getDay0() + 1);
                }
                if (exeTime >= nowTime + 86400 && exeTime < nowTime + 86400 * 2) {
                    countTestInAWeekDto.setDay1(countTestInAWeekDto.getDay1() + 1);
                }
                if (exeTime >= nowTime + 86400 * 2 && exeTime < nowTime + 86400 * 3) {
                    countTestInAWeekDto.setDay2(countTestInAWeekDto.getDay2() + 1);
                }
                if (exeTime >= nowTime + 86400 * 3 && exeTime < nowTime + 86400 * 4) {
                    countTestInAWeekDto.setDay3(countTestInAWeekDto.getDay3() + 1);
                }
                if (exeTime >= nowTime + 86400 * 4 && exeTime < nowTime + 86400 * 5) {
                    countTestInAWeekDto.setDay4(countTestInAWeekDto.getDay4() + 1);
                }
                if (exeTime >= nowTime + 86400 * 5 && exeTime < nowTime + 86400 * 6) {
                    countTestInAWeekDto.setDay5(countTestInAWeekDto.getDay5() + 1);
                }
                if (exeTime >= nowTime + 86400 * 6 && exeTime < nowTime + 86400 * 7) {
                    countTestInAWeekDto.setDay6(countTestInAWeekDto.getDay6() + 1);
                }
                if (exeTime >= nowTime + 86400 * 7 && exeTime < nowTime + 86400 * 8) {
                    countTestInAWeekDto.setDay7(countTestInAWeekDto.getDay7() + 1);
                }
            }
            countTestInAWeekDtoList.add(countTestInAWeekDto);
        }
        return countTestInAWeekDtoList;
    }
}
