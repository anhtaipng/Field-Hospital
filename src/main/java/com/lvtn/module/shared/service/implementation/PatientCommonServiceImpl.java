package com.lvtn.module.shared.service.implementation;

import com.lvtn.module.admin.common.ApiAdminMesssage;
import com.lvtn.module.patient.common.ApiPatientMesssage;
import com.lvtn.module.shared.common.ApiSharedMesssage;
import com.lvtn.module.shared.dto.*;
import com.lvtn.module.shared.enumeration.*;
import com.lvtn.module.shared.mapper.CommonMapper;
import com.lvtn.module.shared.mapper.PatientMapper;
import com.lvtn.module.shared.model.*;
import com.lvtn.module.shared.repository.*;
import com.lvtn.module.shared.service.NotificationService;
import com.lvtn.module.shared.service.PatientCommonService;
import com.lvtn.module.user.dto.PushNotificationRequest;
import com.lvtn.module.user.service.implementation.PushNotificationService;
import com.lvtn.platform.common.PageResponse;
import com.lvtn.platform.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class PatientCommonServiceImpl implements PatientCommonService {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    PatientLocationRepository patientLocationRepository;

    @Autowired
    PatientMapper patientMapper;

    @Autowired
    SickbedRepository sickbedRepository;

    @Autowired
    DependentRepository dependentRepository;

    @Autowired
    ExaminationRepository examinationRepository;

    @Autowired
    PrescriptionRepository prescriptionRepository;

    @Autowired
    AllergyRepisitory allergyRepisitory;

    @Autowired
    SicknessRepository sicknessRepository;

    @Autowired
    SurgeryRepository surgeryRepository;

    @Autowired
    VaccineRepository vaccinerepository;

    @Autowired
    TestResultRepository testResultRepository;

    @Autowired
    RequirementRepository requirementRepository;

    @Autowired
    PrescriptionDetailRepository prescriptionDetailRepository;

    @Autowired
    MedicineRepository medicineRepository;

    @Autowired
    MedicineBatchRepository medicineBatchRepository;

    @Autowired
    TestTypeRepository testTypeRepository;

    @Autowired
    StatisticTypeRepository statisticTypeRepository;

    @Autowired
    FloorRepository floorRepository;
    @Autowired
    BuildingRepository buildingRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    RoomTypeRepository roomTypeRepository;

    @Autowired
    BvdcGroupRepository bvdcGroupRepository;

    @Autowired
    MedicineBatchHistoryRepository medicineBatchHistoryRepository;

    @Autowired
    PatientStatusRepository patientStatusRepository;

    @Autowired
    CommonMapper commonMapper;

    @Autowired
    NotificationService notificationService;

    @Autowired
    CovidHospitalRepository covidHospitalRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PushNotificationService pushNotificationService;

    @Override
    public void support(String supportUsername, String username) {
        User user = userRepository.findUserByUsername(supportUsername);
        if (user==null) {
            throw new ApiException(4411,"Không tìm thấy người hỗ trợ");
        }
        if (user.getFcmToken()==null) {
            throw new ApiException(4411,"Không thông báo được với người hỗ trợ");
        }

        Patient patient = patientRepository.findPatientByUser_Username(username);
        if (patient==null) {
            throw new ApiException(ApiSharedMesssage.PATIENT_NOT_FOUND);
        }

        PatientLocation patientLocation = patientLocationRepository.findByPatient_User_UsernameAndEndTimeIsNull(username);
        if (user.getFcmToken()!=null) {
            PushNotificationRequest pushNotificationRequest = new PushNotificationRequest();
            pushNotificationRequest.setTitle("Bệnh nhân: " + patient.getUser().getName() + " (" + username + ")" + " yêu cầu hỗ trợ");
            pushNotificationRequest.setToken(user.getFcmToken());
            if (patientLocation != null) {
                PatientLocationDto patientLocationDto = commonMapper.mapPatientLocationToPatientLocationResponseDto(patientLocation);
                pushNotificationRequest.setMessage("Vị trí giường bệnh: Tòa nhà " + patientLocationDto.getBuildingName() + ", Tầng " + patientLocationDto.getFloorNo() + ", Phòng " + patientLocationDto.getRoomNo() + " (" + patientLocationDto.getRoomType() + ")" + ", Giường " + patientLocationDto.getSickbedNo());
            } else {
                pushNotificationRequest.setMessage("");
            }
            pushNotificationService.sendPushNotificationToToken(pushNotificationRequest);
        }
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setTitle("Bệnh nhân " + patient.getUser().getName() + " ("+username+")" + " vừa yêu cầu hỗ trợ");
        notificationDto.setDescription("Bệnh nhân yêu cầu nhân viên y tế " + user.getName() + " hỗ trợ");
        notificationDto.setCmnd(username);
        notificationDto.setNotificationType(NotificationType.H_SUPPORT);
        notificationService.addNotification(notificationDto,patient,false,true,true);
    }


    @Override
    public PatientInfoDto getPatientInfo(String patient_cmnd) {
        Patient patient = patientRepository.findPatientByUser_Username(patient_cmnd);
        if (patient == null) {
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        return patientMapper.mapPatientToPatientInfoResponseDto(patient);
    }

    @Override
    public PatientInfoDto updatePatientInfo(PatientInfoDto patientInfoDto, String patient_cmnd) {
        Patient patient = patientRepository.findPatientByUser_Username(patient_cmnd);
        if (patient == null) {
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        patient.setBhyt(patientInfoDto.getBhyt());
        patient.getUser().setEmail(patientInfoDto.getEmail());
        if (patientInfoDto.getAvatarURL()!=null) {
            patient.getUser().setAvatarURL(patientInfoDto.getAvatarURL());
        }
        if (patientInfoDto.getGender()!=null) {
            patient.getUser().setGender(patientInfoDto.getGender());
        }
        if (patientInfoDto.getBirthDate()!=null) {
            patient.getUser().setBirthDate(patientInfoDto.getBirthDate());
        }
        if (patientInfoDto.getName()!=null) {
            patient.getUser().setName(patientInfoDto.getName());
        }
        if (patientInfoDto.getPhone()!=null) {
            patient.getUser().setPhone(patientInfoDto.getPhone());
        }
        if (patientInfoDto.getDischargeDate()!=null) {
            patient.setDischargeDate(patientInfoDto.getDischargeDate());
        }
        if (patientInfoDto.getHospitalizedDate()!=null) {
            patient.setHospitalizedDate(patientInfoDto.getDischargeDate());
        }
        patient.setThon(patientInfoDto.getThon());
        if (patientInfoDto.getTinh()!=null) {
            patient.setTinh(patientInfoDto.getTinh());
        }
        if (patientInfoDto.getXa()!=null) {
            patient.setXa(patientInfoDto.getXa());
        }
        if (patientInfoDto.getHuyen()!=null) {
            patient.setHuyen(patientInfoDto.getHuyen());
        }
        return patientMapper.mapPatientToPatientInfoResponseDto(patientRepository.save(patient));
    }

    @Override
    public PatientLocationDto updateCurrentLocation(PatientLocationDto patientLocationDto, String patient_cmnd) {
        Patient patient = patientRepository.findPatientByUser_Username(patient_cmnd);
        if (patient == null) {
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        if (patientLocationDto.getSickbedNo()==null || patientLocationDto.getBuildingName()==null || patientLocationDto.getFloorNo() == null || patientLocationDto.getRoomNo() == null) {
            throw new ApiException(ApiSharedMesssage.LOCATION_NOT_FOUND);
        }
        Optional<Sickbed> sickbed = sickbedRepository.findById(commonMapper.mapPatientLocationDtoToSickbedPK(patientLocationDto));
        if (sickbed.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.LOCATION_NOT_FOUND);
        }
        if (sickbed.get().getSickbedStatus()== SickbedStatus.USED) {
            throw new ApiException(ApiSharedMesssage.LOCATION_NOT_AVAILABLE);
        }
        List<Requirement> requirementList = requirementRepository.findByStatusAndRequirementTypeAndPatient_User_UsernameOrderByCreateTimeDesc(RequirementStatus.DANG_THUC_HIEN,RequirementType.B_SICKBED,patient_cmnd);
        boolean checkRequireBelong = false;
        boolean hasRequest = false;
        if (requirementList!=null && !requirementList.isEmpty()) {
            hasRequest = true;
            for (Requirement r : requirementList) {
                PatientLocationDto locationRequired = commonMapper.mapSickbedToPatientLocationResponseDto(r.getSickbed());
                if (sickbed.get().getSickbedStatus() == SickbedStatus.REQUESTED && locationRequired.getBuildingName().equals(patientLocationDto.getBuildingName()) && locationRequired.getFloorNo().equals(patientLocationDto.getFloorNo()) && locationRequired.getSickbedNo().equals(patientLocationDto.getSickbedNo()) && locationRequired.getRoomNo().equals(patientLocationDto.getRoomNo())) {
                    checkRequireBelong = true;
                    r.setStatus(RequirementStatus.HOAN_THANH);
                    r.setExecutionTime(new Date());
                    requirementRepository.save(r);
                }
            }
        }
        if (hasRequest && !checkRequireBelong) {
            throw new ApiException(123123, "Bệnh nhân cập nhật vị trí không đúng yêu cầu");
        }
        if (sickbed.get().getSickbedStatus() == SickbedStatus.REQUESTED && !checkRequireBelong) {
            throw new ApiException(ApiSharedMesssage.SICKBED_REQUESTED);
        }

        if (sickbed.get().getSickbedStatus()==SickbedStatus.DISABLE) {
            throw new ApiException(12312321,"Giường bệnh không còn được sử dụng");
        }
        PatientLocation oldPatientLocation = patientLocationRepository.findByPatient_User_UsernameAndEndTimeIsNull(patient_cmnd);
        if (oldPatientLocation!=null) {
            oldPatientLocation.setEndTime(new Date());
            patientLocationRepository.save(oldPatientLocation);
            PatientLocationDto oldSickbed = commonMapper.mapPatientLocationToPatientLocationResponseDto(oldPatientLocation);
            sickbedRepository.updateSickbedStatus(String.valueOf(SickbedStatus.EMPTY),oldSickbed.getSickbedNo(),oldSickbed.getRoomNo(),
                    oldSickbed.getFloorNo(),oldSickbed.getBuildingName());

        }

        sickbedRepository.updateSickbedStatus(String.valueOf(SickbedStatus.USED),patientLocationDto.getSickbedNo(),patientLocationDto.getRoomNo(),patientLocationDto.getFloorNo(),
                patientLocationDto.getBuildingName());

        PatientLocation newPatientLocation = commonMapper.mapPatientLocationDtoToPatientLocation(patient,sickbed.get(),patientLocationDto);
        newPatientLocation.setStartTime(new Date());

        PatientLocation patientLocation = patientLocationRepository.save(newPatientLocation);

        PatientLocationDto newPatientLocationDto = commonMapper.mapPatientLocationToPatientLocationResponseDto(patientLocation);

//        List<Requirement> requirements = requirementRepository.findByStatusAndRequirementTypeAndPatient_User_UsernameOrderByCreateTimeDesc(RequirementStatus.DANG_THUC_HIEN,RequirementType.B_SICKBED,patient_cmnd);
//        if (requirements!=null && !requirements.isEmpty()) {
//            for (Requirement requirement : requirements) {
//                if (requirement.getSickbed().getRoom().getRoomType().getRoomType().equals(newPatientLocationDto.getRoomType())) {
//
//                }
//            }
//        }
        if (patient.getCurrentPatientStatus().equals(PatientStatusType.REQUEST_NAM_VIEN)) {
            patient.setCurrentPatientStatus(PatientStatusType.NAM_VIEN);
            patient.setPatientStatus(patientStatusRepository.getById(PatientStatusType.NAM_VIEN));
            patientRepository.save(patient);
        }

        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setNotificationType(NotificationType.B_SICKBED);
        notificationDto.setTitle("Bệnh nhân " + patient.getUser().getName() + " ("+patient_cmnd+")" + " vừa thay đổi giường bệnh");
        notificationDto.setDescription("Giường bệnh mới có vị trí: Tòa nhà "+newPatientLocationDto.getBuildingName()+", Tầng "+newPatientLocationDto.getFloorNo()+", Phòng "+newPatientLocationDto.getRoomNo()+" ("+newPatientLocationDto.getRoomType()+"), "+"Giường số "+newPatientLocationDto.getSickbedNo());
        notificationService.addNotification(notificationDto,patient,true,true,true);

        return commonMapper.mapPatientLocationToPatientLocationResponseDto(patientLocation);
    }

    @Override
    public PatientLocationDto getPatientCurrentLocation(String patient_cmnd) {
        if (patientRepository.findPatientByUser_Username(patient_cmnd)==null) {
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }

        PatientLocation patientLocation = patientLocationRepository.findByPatient_User_UsernameAndEndTimeIsNull(patient_cmnd);
        if (patientLocation == null) {
            throw new ApiException(ApiPatientMesssage.PATIENT_LOCATION_NOT_FOUND);
        }
        return commonMapper.mapPatientLocationToPatientLocationResponseDto(patientLocation);
    }

    @Override
    public PatientContactResponseDto getPatientContact(String patient_cmnd) {
        Patient patient = patientRepository.findPatientByUser_Username(patient_cmnd);
        if (patient == null) {
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        return patientMapper.mapPatientToPatientContactResponseDto(patient);
    }

    @Override
    public DependentDto updatePatientDependent(DependentDto dependentDto, String patient_cmnd) {
        Patient patient = patientRepository.findPatientByUser_Username(patient_cmnd);
        if (patient == null) {
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        Optional<Dependent> dependent = dependentRepository.findById(new DependentPK(dependentDto.getName(),patient.getId()));
        if (dependent.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.DEPENDENT_NOT_FOUND);
        }

        return commonMapper.mapDependentToDependentDto(dependentRepository.save(commonMapper.mapDependentDtoToDependent(patient,dependentDto)));
    }

    @Override
    public void deletePatientDependent(DependentDto dependentDto, String patient_cmnd) {
        Patient patient = patientRepository.findPatientByUser_Username(patient_cmnd);
        if (patient == null) {
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        DependentPK dependentPK = new DependentPK(dependentDto.getName(),patient.getId());
        Optional<Dependent> dependent = dependentRepository.findById(dependentPK);

        if (dependent.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.DEPENDENT_NOT_FOUND);
        }
        dependentRepository.deleteById(dependentPK);
    }

    @Override
    public DependentDto addPatientDependent(DependentDto dependentDto, String patient_cmnd) {
        Patient patient = patientRepository.findPatientByUser_Username(patient_cmnd);
        if (patient == null) {
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        Optional<Dependent> dependent = dependentRepository.findById(new DependentPK(dependentDto.getName(),patient.getId()));
        if (dependent.isPresent()) {
            throw new ApiException(ApiSharedMesssage.DEPENDENT_EXISTED);
        }
        return commonMapper.mapDependentToDependentDto(dependentRepository.save(commonMapper.mapDependentDtoToDependent(patient,dependentDto)));
    }

    @Override
    public PrescriptionResponseDto getCurrentPrescription(String patient_cmnd) {
        Patient patient = patientRepository.findPatientByUser_Username(patient_cmnd);
        if (patient == null) {
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        Prescription prescription = prescriptionRepository.findFirstByPatientOrderByCreateTimeDesc(patient);
        if (prescription==null) {
            throw new ApiException(ApiSharedMesssage.PRESCRIPTION_NOT_FOUND);
        }
        if (prescription.getEndTime().before(new Date())) {
            throw new ApiException(ApiSharedMesssage.PRESCRIPTION_NOT_FOUND);
        }
        List<PrescriptionDetail> prescriptionDetailList = prescriptionDetailRepository.findByPrescription(prescription);
        Examination examination = examinationRepository.findByPrescription(prescription);
        return commonMapper.mapPrescriptionToPrescriptionResponseDto(prescription,prescriptionDetailList,examination);
    }

    @Override
    public PageResponse<ExaminationDto> getExamination(ExaminationListRequestDto examinationListRequestDto, String patient_cmnd) {
        Patient patient = patientRepository.findPatientByUser_Username(patient_cmnd);
        if (patient == null) {
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        Page<Examination> examinations;
        Pageable pageable = PageRequest.of(examinationListRequestDto.getPageNum(),examinationListRequestDto.getPageSize());
        if (examinationListRequestDto.isSearch()) {
            examinations = examinationRepository.findByPatient_User_UsernameAndExaminationTimeBetweenOrderByExaminationTimeDesc(patient_cmnd, examinationListRequestDto.getFromDate(), examinationListRequestDto.getToDate(),pageable);
        } else {
            examinations = examinationRepository.findByPatient_User_UsernameOrderByExaminationTimeDesc(patient_cmnd,pageable);
        }
        return PageResponse.buildPageResponse(examinations.map(commonMapper::mapExaminationToExaminationResponseDto));
    }

    @Override
    public List<AllergyDto> getAllergy(String username) {
        Patient patient = patientRepository.findPatientByUser_Username(username);
        if (patient == null) {
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        List<AllergyDto> allergyDtos = allergyRepisitory.findByPatient_User_Username(username).stream().map((x) -> patientMapper.mapAllergyToAllergyDto(x)).collect(Collectors.toList());
        return allergyDtos;
    }

    @Override
    public List<SicknessDto> getSickness(String username) {
        Patient patient = patientRepository.findPatientByUser_Username(username);
        if (patient == null) {
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        List<SicknessDto> sicknessDtos = sicknessRepository.findByPatient_User_Username(username).stream().map((x) -> patientMapper.mapSicknessToSicknessDto(x)).collect(Collectors.toList());
        return sicknessDtos;
    }

    @Override
    public List<SurgeryDto> getSurgery(String username) {
        Patient patient = patientRepository.findPatientByUser_Username(username);
        if (patient == null) {
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        List<SurgeryDto> surgeryDtos = surgeryRepository.findByPatient_User_Username(username).stream().map((x) -> patientMapper.mapSurgeryToSurgeryDto(x)).collect(Collectors.toList());
        return surgeryDtos;
    }

    @Override
    public List<VaccineDto> getVaccine(String username) {
        Patient patient = patientRepository.findPatientByUser_Username(username);
        if (patient == null) {
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        List<VaccineDto> vaccineDtos = vaccinerepository.findByPatient_User_UsernameOrderByNoInjection(username).stream().map((x) -> patientMapper.mapVaccineToVaccineDto(x)).collect(Collectors.toList());
        return vaccineDtos;
    }

    @Override
    public AllergyDto addAllergy(AllergyDto allergyDto, String username) {
        allergyDto.setId(null);
        Patient patient = patientRepository.findPatientByUser_Username(username);
        if (patient == null){
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        return patientMapper.mapAllergyToAllergyDto(allergyRepisitory.save(patientMapper.mapAllergyDtoToAllergy(allergyDto, patient)));
    }

    @Override
    public AllergyDto updateAllergy(AllergyDto allergyDto, String username) {
        Patient patient = patientRepository.findPatientByUser_Username(username);
        if (patient == null){
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        return patientMapper.mapAllergyToAllergyDto(allergyRepisitory.save(patientMapper.mapAllergyDtoToAllergy(allergyDto, patient)));
    }

    @Override
    public AllergyDto deleteAllergy(Long idAllergy) {
        Optional<Allergy> allergy = allergyRepisitory.findById(idAllergy);
        if (allergy.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.ALLERGY_NOT_FOUND);
        }
        allergyRepisitory.delete(allergy.get());
        return null;
    }

    @Override
    public SicknessDto addSickness(SicknessDto sicknessDto, String username) {
        sicknessDto.setId(null);
        Patient patient = patientRepository.findPatientByUser_Username(username);
        if (patient == null){
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        return patientMapper.mapSicknessToSicknessDto(sicknessRepository.save(patientMapper.mapSicknessDtoDtoToSickness(sicknessDto, patient)));
    }

    @Override
    public SicknessDto updateSickness(SicknessDto sicknessDto, String username) {
        Patient patient = patientRepository.findPatientByUser_Username(username);
        if (patient == null){
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        return patientMapper.mapSicknessToSicknessDto(sicknessRepository.save(patientMapper.mapSicknessDtoDtoToSickness(sicknessDto, patient)));
    }

    @Override
    public SicknessDto deleteSickness(Long idSickness) {
        Optional<Sickness> sickness = sicknessRepository.findById(idSickness);
        if (sickness.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.SICKNESS_NOT_FOUND);
        }
        sicknessRepository.delete(sickness.get());
        return null;
    }

    @Override
    public SurgeryDto addSurgery(SurgeryDto surgeryDto, String username) {
        surgeryDto.setId(null);
        Patient patient = patientRepository.findPatientByUser_Username(username);
        if (patient == null){
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        return patientMapper.mapSurgeryToSurgeryDto(surgeryRepository.save(patientMapper.mapSurgeryDtoToSurgery(surgeryDto, patient)));
    }

    @Override
    public SurgeryDto updateSurgery(SurgeryDto surgeryDto, String username) {
        Patient patient = patientRepository.findPatientByUser_Username(username);
        if (patient == null){
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        return patientMapper.mapSurgeryToSurgeryDto(surgeryRepository.save(patientMapper.mapSurgeryDtoToSurgery(surgeryDto, patient)));
    }

    @Override
    public SurgeryDto deleteSurgery(Long idSurgery) {
        Optional<Surgery> surgery = surgeryRepository.findById(idSurgery);
        if (surgery.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.SURGERY_NOT_FOUND);
        }
        surgeryRepository.delete(surgery.get());
        return null;
    }

    @Override
    public VaccineDto addVaccine(VaccineDto vaccineDto, String username) {
        vaccineDto.setId(null);
        Patient patient = patientRepository.findPatientByUser_Username(username);
        if (patient == null){
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        Vaccine vaccine = vaccinerepository.findByNoInjectionAndPatient(vaccineDto.getNoInjection(),patient);
        if (vaccine != null) {
            vaccine.setVaccineType(vaccineDto.getVaccineType());
            vaccine.setInjectionPlace(vaccineDto.getInjectionPlace());
            vaccine.setInjectionTime(vaccineDto.getInjectionTime());
        } else {
            vaccine = patientMapper.mapVaccineDtoToVaccine(vaccineDto,patient);
        }

        return patientMapper.mapVaccineToVaccineDto(vaccinerepository.save(vaccine));
    }

    @Override
    public VaccineDto updateVaccine(VaccineDto vaccineDto, String username) {
        Patient patient = patientRepository.findPatientByUser_Username(username);
        if (patient == null){
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        return patientMapper.mapVaccineToVaccineDto(vaccinerepository.save(patientMapper.mapVaccineDtoToVaccine(vaccineDto, patient)));
    }

    @Override
    public VaccineDto deleteVaccine(Long idVaccine) {
        Optional<Vaccine> vaccine = vaccinerepository.findById(idVaccine);
        if (vaccine.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.VACCINE_NOT_FOUND);
        }
        vaccinerepository.delete(vaccine.get());
        return null;
    }

    @Override
    public PageResponse<TestResultDto> getTestResult(TestResultRequest testResultRequest,String username) {
        Patient patient = patientRepository.findPatientByUser_Username(username);
        if (patient == null) {
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        Pageable pageable = PageRequest.of(testResultRequest.getPageNum(),testResultRequest.getPageSize());
        Page<TestResult> testResultDtos;
        if (testResultRequest.getTestType()==null||testResultRequest.getTestType().equals("")) {
             testResultDtos = testResultRepository.findByPatient_User_UsernameOrderByTimeCreatedDesc(username,pageable);
        } else {
            testResultDtos = testResultRepository.findByTestType_TestTypeAndPatient_User_UsernameOrderByTimeCreatedDesc(testResultRequest.getTestType(),username,pageable);
        }
        return PageResponse.buildPageResponse(testResultDtos.map(patientMapper::mapTestResultToTestResultDto));
    }

    @Override
    public TestResultDto addTestResult(TestResultDto testResultDto, String username) {
        testResultDto.setId(null);

        if (testResultDto.getTimeCreated()==null) {
            testResultDto.setTimeCreated(new Date());
        }

        Patient patient = patientRepository.findPatientByUser_Username(username);
        if (patient == null){
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        TestType testType = testTypeRepository.findByTestType(testResultDto.getTestType());
        if (testType == null){
            throw new ApiException((ApiSharedMesssage.TEST_TYPE_NOT_FOUND));
        }

        TestResult testResult = testResultRepository.save(patientMapper.mapTestResultDtoToTestResult(testResultDto, patient, testType));
        List<Requirement> requirements = requirementRepository.findByStatusAndRequirementTypeAndPatient_User_UsernameOrderByCreateTimeDesc(RequirementStatus.DANG_THUC_HIEN,RequirementType.C_TEST,username);
        if (requirements!=null && !requirements.isEmpty()) {
            for (Requirement requirement : requirements) {
                if ((testResult.getTestType().getTestType().contains("PCR") || requirement.getTestType().getTestType().equals(testResultDto.getTestType())) && requirement.getCreateTime().before(testResultDto.getTimeCreated())) {
                    requirement.setStatus(RequirementStatus.HOAN_THANH);
                    requirement.setExecutionTime(new Date());
                    requirementRepository.save(requirement);
                }
            }
        }

        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setNotificationType(NotificationType.C_TEST);
        notificationDto.setTitle("Bệnh nhân " + patient.getUser().getName() + " ("+username+")" + " vừa có kết quả xét nghiệm");
        notificationDto.setDescription("Loại xét nghiệm: "+testResult.getTestType().getTestType()+".\nKết quả: "+(testResult.getResult()?"Dương tính":"Âm tính") + (testResult.getValue()!=null?". Giá trị CT: "+testResult.getValue():""));
        notificationService.addNotification(notificationDto,patient,true,false,true);

        if (testResult.getResult()) {
            if (!testResult.getTestType().getTestType().contains("PCR")) {
                patient.setPositiveDate(testResult.getTimeCreated());
            } else if (testResult.getValue()<30) {
                patient.setPositiveDate(testResult.getTimeCreated());
            }
            patientRepository.save(patient);
            Date newTestDate = new Date(testResultDto.getTimeCreated().getTime()+1000*60*60*24*3);
            List<Requirement> requirementList = requirementRepository.findByStatusAndRequirementTypeAndPatient_User_UsernameAndCreateTimeBefore(RequirementStatus.DANG_THUC_HIEN,RequirementType.C_TEST,username,newTestDate);
            String doTestType = testType.getTestType();
            if (requirementList!=null && requirementList.size()>0) {
                for (Requirement requirement : requirementList) {
                        requirement.setStatus(RequirementStatus.HUY_BO);
                        requirementRepository.save(requirement);
                    if (requirement.getTestType().getTestType().contains("PCR")) {
                        doTestType = requirement.getTestType().getTestType();
                    }
                }
            }
            RequirementDto requirementDto = new RequirementDto();
            requirementDto.setRequirementType(RequirementType.C_TEST);
            requirementDto.setTestType(doTestType);
            requirementDto.setStatus(RequirementStatus.DANG_THUC_HIEN);
            requirementDto.setCreateTime(newTestDate);
            requirementDto.setAuto(true);
            addRequirement(requirementDto,username);
        }

        return patientMapper.mapTestResultToTestResultDto(testResult);
    }

    @Override
    public TestResultDto updateTestResult(TestResultDto testResultDto, String username) {
        Patient patient = patientRepository.findPatientByUser_Username(username);
        if (patient == null){
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        TestType testType = testTypeRepository.findByTestType(testResultDto.getTestType());
        if (testType == null){
            throw new ApiException((ApiSharedMesssage.TEST_TYPE_NOT_FOUND));
        }
        return patientMapper.mapTestResultToTestResultDto(testResultRepository.save(patientMapper.mapTestResultDtoToTestResult(testResultDto, patient, testType)));
    }

    @Override
    public TestResultDto deleteTestResult(Long idTestReult) {
        Optional<TestResult> testResult = testResultRepository.findById(idTestReult);
        if (testResult.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.TEST_RESULT_NOT_FOUND);
        }
        testResultRepository.delete(testResult.get());
        return null;
    }


    public void checkRequirement(RequirementDto requirementDto,Patient patient) {

        if (requirementDto.getRequirementType()==RequirementType.A_PATIENT_STATUS) {
            if (patient.getPatientStatus().getPatientStatusType() != PatientStatusType.NAM_VIEN) {
                throw new ApiException(ApiSharedMesssage.STATUS_CHANGED);
            }
        }

        if (requirementDto.getRequirementType()== RequirementType.C_TEST) {
            Optional<TestType> testType = testTypeRepository.findById(requirementDto.getTestType());
            if (testType.isEmpty()) {
                throw new ApiException(ApiSharedMesssage.TEST_TYPE_NOT_FOUND);
            }
        }

        if (requirementDto.getRequirementType()== RequirementType.D_STATISTIC) {
            Optional<StatisticType> statisticType;
            if (requirementDto.getStatisticType().equals("Huyết áp")) {
                statisticType = statisticTypeRepository.findById("Huyết áp tâm thu");
            } else {
                statisticType = statisticTypeRepository.findById(requirementDto.getStatisticType());
            }
            if (statisticType.isEmpty()) {
                throw new ApiException(ApiSharedMesssage.STATISTIC_TYPE_NOT_FOUND);
            }
        }

        if (requirementDto.getRequirementType() == RequirementType.A_PATIENT_STATUS) {
            Optional<PatientStatus> patientStatus = patientStatusRepository.findById(requirementDto.getPatientStatusType());
            if (patientStatus.isEmpty()) {
                throw new ApiException(ApiSharedMesssage.PATIENT_STATUS_NOT_FOUND);
            }
            if (requirementDto.getPatientStatusType().equals(PatientStatusType.CHUYEN_VIEN)) {
                if (requirementDto.getCovidHospital() == null) {
                    throw new ApiException(13212312, "Xin vui lòng chọn bệnh viện tiếp nhận");
                } else if (covidHospitalRepository.findById(requirementDto.getCovidHospital()).isEmpty()) {
                    throw new ApiException(13212312, "Không tìm thấy bệnh viện tiếp nhận");
                }
            }
        }

        if (requirementDto.getRequirementType()== RequirementType.B_SICKBED) {
            Optional<Building> building = buildingRepository.findById(requirementDto.getPatientLocation().getBuildingName());
            if (building.isEmpty()) {
                throw new ApiException(ApiSharedMesssage.BUILDING_NOT_FOUND);
            }
            Optional<Floor> floor = floorRepository.findById(new FloorPK(requirementDto.getPatientLocation().getFloorNo(),requirementDto.getPatientLocation().getBuildingName()));
            if (floor.isEmpty()) {
                throw new ApiException(ApiSharedMesssage.FLOOR_NOT_FOUND);
            }

            Optional<Room> room = roomRepository.findById(new RoomPK(requirementDto.getPatientLocation().getRoomNo(),floor.get()));
            if (room.isEmpty()) {
                throw new ApiException(ApiSharedMesssage.ROOM_NOT_FOUND);
            }

            Optional<RoomType> roomType = roomTypeRepository.findById(requirementDto.getPatientLocation().getRoomType());
            if (roomType.isEmpty()) {
                throw new ApiException(ApiSharedMesssage.ROOM_TYPE_NOT_FOUND);
            }

            Optional<Sickbed> sickbed = sickbedRepository.findById(new SickbedPK(requirementDto.getPatientLocation().getSickbedNo(),room.get()));
            if (sickbed.isEmpty()) {
                throw new ApiException(ApiSharedMesssage.SICKBED_NOT_FOUND);
            }

            if (sickbed.get().getSickbedStatus().equals(SickbedStatus.USED)) {
                throw new ApiException(ApiSharedMesssage.SICKBED_USED);
            }

            if (sickbed.get().getSickbedStatus().equals(SickbedStatus.REQUESTED)) {
                throw new ApiException(ApiSharedMesssage.SICKBED_REQUESTED);
            }
        }
    }

    @Override
    public RequirementDto addRequirement(RequirementDto requirementDto, String username) {

        Patient patient = patientRepository.findPatientByUser_Username(username);
        if (patient == null){
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }

        checkRequirement(requirementDto,patient);
        Requirement requirement = requirementRepository.save(commonMapper.mapRequirementDtoToRequirement(requirementDto,patient,null));

        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setTitle("Bệnh nhân " + patient.getUser().getName() + " ("+username+") " + "có yêu cầu mới");
        notificationDto.setNotificationType(NotificationType.E_REQUIREMENT);
        notificationDto.setDescription("Loại yêu cầu: "+(requirement.getRequirementType().equals(RequirementType.C_TEST)?"Xét nghiệm Covid-19 ("+requirement.getTestType().getTestType()+")":requirement.getRequirementType().equals(RequirementType.B_SICKBED)?"Thay đổi phòng bệnh sang phòng "+requirement.getSickbed().getRoom().getRoomType():requirement.getRequirementType().equals(RequirementType.A_PATIENT_STATUS)? "" + (requirement.getPatientStatus().getPatientStatusType().equals(PatientStatusType.CHUYEN_VIEN)?"Thực hiện chuyển viện":requirement.getPatientStatus().getPatientStatusType().equals(PatientStatusType.XUAT_VIEN)?"Thực hiện xuất viện":requirement.getPatientStatus().getPatientStatusType().equals(PatientStatusType.TU_VONG)?"Xác nhận tử vọng":"Xác nhận nằm viện"):"Cập nhật chỉ số sức khỏe "+requirement.getStatisticTypes().getStatisticType()));
        notificationService.addNotification(notificationDto,patient,true,true,false);
        return commonMapper.mapRequirementToRequirementResponseDto(requirement);

    }



    @Override
    public PrescriptionDetailDto addPrescriptionDetail(PrescriptionDetailDto prescriptionDetailDto, Prescription prescription) {
        Optional<Medicine> medicine = medicineRepository.findById(prescriptionDetailDto.getMedicine().getMedicineName());
        if (medicine.isEmpty()) {
            throw new ApiException(ApiAdminMesssage.MEDICINE_NOT_FOUND);
        }

        Integer prescriptionQuantity = prescriptionDetailDto.getQuantity();

        while (prescriptionQuantity>0) {
            MedicineBatch medicineBatch = medicineBatchRepository.findFirstByMedicineNameAndAvailableQuantityGreaterThanAndExpiredDateAfterOrderByExpiredDateAsc(medicine.get(),0L, new Date());
            if (medicineBatch==null) {
                throw new ApiException(ApiSharedMesssage.MEDICINE_NOT_ENOUGH);
            }
            MedicineBatchHistory medicineBatchHistory = new MedicineBatchHistory();
            medicineBatchHistory.setMedicineBatch(medicineBatch);
            medicineBatchHistory.setPrescription(prescription);
            medicineBatchHistory.setTimeCreated(new Date());
            Long medicineBatchAvailable = medicineBatch.getAvailableQuantity();
            if (medicineBatchAvailable>=prescriptionQuantity) {
                medicineBatch.setAvailableQuantity(medicineBatchAvailable-prescriptionQuantity);
                medicineBatchHistory.setQuantityDeducted(prescriptionQuantity);
                prescriptionQuantity = 0;
            } else {
                medicineBatchHistory.setQuantityDeducted(medicineBatchAvailable.intValue());
                medicineBatch.setAvailableQuantity(0L);
                prescriptionQuantity = prescriptionQuantity-medicineBatchAvailable.intValue();
            }
            medicineBatchHistoryRepository.save(medicineBatchHistory);
            medicineBatchRepository.save(medicineBatch);
        }

        Optional<PrescriptionDetail> prescriptionDetail = prescriptionDetailRepository.findById(new PrescriptionDetailPK(prescription.getId(),prescriptionDetailDto.getMedicine().getMedicineName()));
        if (prescriptionDetail.isPresent()) {
            throw new ApiException(ApiSharedMesssage.PRESCRIPTION_DETAIL_EXISTED);
        }

        return commonMapper.mapPrescriptionDetailToPrescriptionDetailResponseDto(prescriptionDetailRepository.save(commonMapper.mapPrescriptionDetailDtoToPrescriptionDetail(prescriptionDetailDto,medicine.get(),prescription)));
    }


}
