package com.lvtn.module.doctor.service.implementation;

import com.google.common.base.Strings;
import com.lvtn.module.doctor.service.DoctorService;
import com.lvtn.module.patient.common.ApiPatientMesssage;
import com.lvtn.module.shared.common.ApiSharedMesssage;
import com.lvtn.module.shared.dto.*;
import com.lvtn.module.shared.enumeration.*;
import com.lvtn.module.shared.mapper.CommonMapper;
import com.lvtn.module.shared.mapper.PatientMapper;
import com.lvtn.module.shared.mapper.SickbedMapper;
import com.lvtn.module.shared.model.*;
import com.lvtn.module.shared.repository.*;
import com.lvtn.module.shared.service.NotificationService;
import com.lvtn.module.shared.service.PatientCommonService;
import com.lvtn.platform.common.PageResponse;
import com.lvtn.platform.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    BvdcGroupRepository bvdcGroupRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    PatientMapper patientMapper;

    @Autowired
    CommonMapper commonMapper;

    @Autowired
    ExaminationRepository examinationRepository;

    @Autowired
    SickbedRepository sickbedRepository;

    @Autowired
    RequirementRepository requirementRepository;

    @Autowired
    MedicineRequirementRepository medicineRequirementRepository;

    @Autowired
    PrescriptionRepository prescriptionRepository;

    @Autowired
    PatientCommonService patientCommonService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    TestResultRepository testResultRepository;

    @Autowired
    SickbedMapper sickbedMapper;

    @Override
    public ExaminationDto createExamination(ExaminationDto examinationDto, String username) {

        Doctor doctor = doctorRepository.findDoctorByUser_Username(username);
        Patient patient = patientRepository.findPatientByUser_Username(examinationDto.getPatient());
        if (patient == null){
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        if (examinationDto.getExaminationTime()==null) {
            examinationDto.setExaminationTime(new Date());
        }
        PatientLocationDto patientLocationDto = null;
        if (examinationDto.getRequirements()!=null) {
            for (RequirementDto r : examinationDto.getRequirements()) {
                patientCommonService.checkRequirement(r,patient);
                if (r.getRequirementType().equals(RequirementType.B_SICKBED)) {
                    patientLocationDto=r.getPatientLocation();
                }
            }
        }
        Examination examination;
        if (examinationDto.getPrescriptionDetail()!=null) {
            Prescription prescription = new Prescription();
            prescription.setTook(false);
            prescription.setPatient(patient);
            prescription.setCreateTime(examinationDto.getExaminationTime());
            Date endDate = new Date();
            for (PrescriptionDetailDto prescriptionDetailDto: examinationDto.getPrescriptionDetail()) {
                if (prescriptionDetailDto.getToDay()!=null) {
                    if (prescriptionDetailDto.getToDay().after(endDate)) {
                        endDate = prescriptionDetailDto.getToDay();
                    }
                } else {
                    Date tempDate = new Date(new Date().getTime() + prescriptionDetailDto.getQuantity() * 24 * 60 * 60 * 1000);
                    if (tempDate.after(endDate)) {
                        endDate = tempDate;
                    }
                }
            }
            prescription.setEndTime(endDate);
            prescriptionRepository.save(prescription);
            List<PrescriptionDetailDto> prescriptionDetailDtoList = examinationDto.getPrescriptionDetail().stream().map(p -> patientCommonService.addPrescriptionDetail(p, prescription)).collect(Collectors.toList());
            examination = commonMapper.mapExaminationDtoToExamination(doctor,patient,prescription,examinationDto);
        } else {
            examination = commonMapper.mapExaminationDtoToExamination(doctor,patient,null,examinationDto);
        }
        if (examinationDto.getGroupType()!=null) {
            PatientInfoDto patientInfoDto = examinationChangeGroupType(examinationDto.getGroupType(),patient,doctor);
        }

        if (patientLocationDto!=null) {
            sickbedRepository.updateSickbedStatus(String.valueOf(SickbedStatus.REQUESTED),patientLocationDto.getSickbedNo(),patientLocationDto.getRoomNo(),patientLocationDto.getFloorNo(),patientLocationDto.getBuildingName());
        }

        NotificationDto notificationDto;

        if (examinationDto.getPrescriptionDetail()!=null) {
            notificationDto = new NotificationDto();
            notificationDto.setTitle("Bệnh nhân " + patient.getUser().getName()+" ("+patient.getUser().getUsername()+")" + " vừa có đơn thuốc mới");
            notificationDto.setDescription("Xem đơn thuốc mới nhất để biết thêm chi tiết");
            notificationDto.setNotificationType(NotificationType.G_PRESCRIPTION);
            notificationService.addNotification(notificationDto,patient,true,true,false);
        }

        if (examinationDto.getRequirements()!=null) {
            for (RequirementDto r : examinationDto.getRequirements()) {
                if (r.getRequirementType().equals(RequirementType.A_PATIENT_STATUS)) {
                    List<Requirement> requirements = requirementRepository.findByStatusAndRequirementTypeAndPatient_User_UsernameOrderByCreateTimeDesc(RequirementStatus.DANG_THUC_HIEN,RequirementType.A_PATIENT_STATUS,examinationDto.getPatient());
                    if (requirements!=null && !requirements.isEmpty()) {
                        for (Requirement requirement: requirements) {
                            requirement.setStatus(RequirementStatus.HUY_BO);
                            requirementRepository.save(requirement);
                        }
                    }
                } else if (r.getRequirementType().equals(RequirementType.B_SICKBED)) {
                    List<Requirement> requirements = requirementRepository.findByStatusAndRequirementTypeAndPatient_User_UsernameOrderByCreateTimeDesc(RequirementStatus.DANG_THUC_HIEN,RequirementType.B_SICKBED,examinationDto.getPatient());
                    if (requirements!=null && !requirements.isEmpty()) {
                        for (Requirement requirement: requirements) {
                            requirement.setStatus(RequirementStatus.HUY_BO);
                            requirementRepository.save(requirement);
                            PatientLocationDto sickbed = commonMapper.mapSickbedToPatientLocationResponseDto(requirement.getSickbed());
                            sickbedRepository.updateSickbedStatus(String.valueOf(SickbedStatus.EMPTY), sickbed.getSickbedNo(), sickbed.getRoomNo(), sickbed.getFloorNo(), sickbed.getBuildingName());
                        }
                    }
                }
                notificationDto = new NotificationDto();
                notificationDto.setNotificationType(NotificationType.E_REQUIREMENT);
                notificationDto.setTitle("Bệnh nhân " + patient.getUser().getName()+" ("+patient.getUser().getUsername()+")" + " vừa có yêu cầu mới từ bác sĩ");
                notificationDto.setDescription("Loại yêu cầu: "+(r.getRequirementType().equals(RequirementType.C_TEST)?"Xét nghiệm Covid-19 ("+r.getTestType()+")":r.getRequirementType().equals(RequirementType.B_SICKBED)?"Thay đổi phòng bệnh sang phòng "+r.getPatientLocation().getRoomType():r.getRequirementType().equals(RequirementType.A_PATIENT_STATUS)? "" + (r.getPatientStatusType().equals(PatientStatusType.CHUYEN_VIEN)?"Thực hiện chuyển viện":r.getPatientStatusType().equals(PatientStatusType.XUAT_VIEN)?"Thực hiện xuất viện":r.getPatientStatusType().equals(PatientStatusType.TU_VONG)?"Xác nhận tử vọng":"Xác nhận nằm viện"):"Cập nhật chỉ số sức khỏe ("+r.getStatisticType()+")"));
                notificationService.addNotification(notificationDto,patient,true,true,false);
            }
        }
        Examination result = examinationRepository.save(examination);

        notificationDto = new NotificationDto();
        notificationDto.setTitle("Bác sĩ " + doctor.getUser().getName() + " vừa khám bệnh cho bệnh nhân "+patient.getUser().getName()+" ("+patient.getUser().getUsername()+")");
        notificationDto.setDescription("Xem lịch sử khám bệnh mới nhất để biết thêm chi tiết");
        notificationDto.setNotificationType(NotificationType.F_EXAMINATION);
        notificationService.addNotification(notificationDto,patient,true,true,false);

        return commonMapper.mapExaminationToExaminationResponseDto(result);
    }

    @Override
    public PageResponse<PatientInfoDto> getPatientList(PatientListRequestDto patientListRequestDto, String username) {
        Page<Patient> patients;
        Pageable pageable = PageRequest.of(patientListRequestDto.getPageNum(), patientListRequestDto.getPageSize());

        if (Strings.isNullOrEmpty(patientListRequestDto.getKeyword())) {
            try {
                BvdcGroup bvdcGroup = bvdcGroupRepository.findByGroupTypeAndDoctors(GroupType.valueOf(patientListRequestDto.getGroupType()),doctorRepository.findDoctorByUser_Username(username));
                if (bvdcGroup==null){
                    return null;
                }
                patients = patientRepository.findByBvdcGroup((bvdcGroup),pageable);
            }
            catch (Exception e){
                patients = patientRepository.findAllByBvdcGroupInOrderById(bvdcGroupRepository.findByDoctors(doctorRepository.findDoctorByUser_Username(username)),pageable);
            }
        } else {
            if (patientListRequestDto.getKeyword().charAt(0)>='0' && patientListRequestDto.getKeyword().charAt(0)<='9') {
                try {
                    patients = patientRepository.findByBvdcGroupAndUser_UsernameContaining(bvdcGroupRepository.findByGroupTypeAndDoctors(GroupType.valueOf(patientListRequestDto.getGroupType()),doctorRepository.findDoctorByUser_Username(username)),patientListRequestDto.getKeyword(),pageable);
                }
                catch (Exception e){
                    patients = patientRepository.findAllByBvdcGroupInAndUser_UsernameContaining(bvdcGroupRepository.findByDoctors(doctorRepository.findDoctorByUser_Username(username)),patientListRequestDto.getKeyword(),pageable);
                }
            }
            else {
                try {
                    patients = patientRepository.findByBvdcGroupAndUser_NameContaining(bvdcGroupRepository.findByGroupTypeAndDoctors(GroupType.valueOf(patientListRequestDto.getGroupType()),doctorRepository.findDoctorByUser_Username(username)),patientListRequestDto.getKeyword(),pageable);
                }
                catch (Exception e){
                    patients = patientRepository.findAllByBvdcGroupInAndUser_NameContaining(bvdcGroupRepository.findByDoctors(doctorRepository.findDoctorByUser_Username(username)),patientListRequestDto.getKeyword(),pageable);
                }
            }
        }
        return PageResponse.buildPageResponse(patients.map(patientMapper::mapPatientToPatientInfoResponseDto));
    }

    @Override
    public PageResponse<ExaminationDto> getExaminationList(ExaminationListRequestDto examinationListRequestDto, String username) {
        Page<Examination> examinations;
        Pageable pageable = PageRequest.of(examinationListRequestDto.getPageNum(),examinationListRequestDto.getPageSize());
        if (examinationListRequestDto.isSearch()) {
            examinations = examinationRepository.findByDoctor_User_UsernameAndExaminationTimeBetweenOrderByExaminationTimeDesc(username, examinationListRequestDto.getFromDate(), examinationListRequestDto.getToDate(),pageable);
        } else {
            examinations = examinationRepository.findByDoctor_User_UsernameOrderByExaminationTimeDesc(username,pageable);
        }
        return PageResponse.buildPageResponse(examinations.map(commonMapper::mapExaminationToExaminationResponseDto));
    }

    @Override
    public MedicineRequirementDto saveMedicineRequirement(MedicineRequirementDto medicineRequirementDto, String username) {
        Doctor doctor = doctorRepository.findDoctorByUser_Username(username);
        return commonMapper.mapMedicineRequirementToMedicineRequirementDto(medicineRequirementRepository.save(commonMapper.mapMedicineRequirementDtoToMedicineRequirement(doctor,medicineRequirementDto)));
    }

    @Override
    public PageResponse<MedicineRequirementDto> getMedicineRequirementList(RequirementListRequestDto requirementListRequestDto, String username) {
        Pageable pageable = PageRequest.of(requirementListRequestDto.getPageNum(),requirementListRequestDto.getPageSize());
        if (requirementListRequestDto.getRequirementStatus()==null) {
            return PageResponse.buildPageResponse(medicineRequirementRepository.findByDoctor_User_Username(username,pageable).map(commonMapper::mapMedicineRequirementToMedicineRequirementDto));
        }
        return  PageResponse.buildPageResponse(medicineRequirementRepository.findByStatusAndDoctor_User_UsernameOrderByRequirementTimeDesc(requirementListRequestDto.getRequirementStatus(),username,pageable).map(commonMapper::mapMedicineRequirementToMedicineRequirementDto));

    }

    @Override
    public PageResponse<RequirementDto> getRequirementList(RequirementListRequestDto requirementListRequestDto, String username) {
        Pageable pageable = PageRequest.of(requirementListRequestDto.getPageNum(),requirementListRequestDto.getPageSize());
        List<Patient> patients = patientRepository.findAllByBvdcGroupIn(bvdcGroupRepository.findByDoctors(doctorRepository.findDoctorByUser_Username(username)));
        if (patients.size()==0) {
            return new PageResponse<>();
        }

        if (requirementListRequestDto.getRequirementStatus()==null && requirementListRequestDto.getRequirementType()==null) {
            return PageResponse.buildPageResponse(requirementRepository.findAllByPatientInOrderByRequirementTypeAscCreateTimeDesc(patients,pageable).map(commonMapper::mapRequirementToRequirementResponseDto));
        } else if (requirementListRequestDto.getRequirementType()!=null && requirementListRequestDto.getRequirementStatus()!=null) {
            return PageResponse.buildPageResponse(requirementRepository.findAllByStatusAndRequirementTypeAndPatientInOrderByRequirementTypeDescCreateTimeDesc(requirementListRequestDto.getRequirementStatus(),requirementListRequestDto.getRequirementType(),patients,pageable).map(commonMapper::mapRequirementToRequirementResponseDto));
        } else if (requirementListRequestDto.getRequirementType()!=null) {
            return PageResponse.buildPageResponse(requirementRepository.findAllByRequirementTypeAndPatientInOrderByRequirementTypeAscCreateTimeDesc(requirementListRequestDto.getRequirementType(),patients,pageable).map(commonMapper::mapRequirementToRequirementResponseDto));
        }

        return PageResponse.buildPageResponse(requirementRepository.findAllByStatusAndPatientInOrderByRequirementTypeAscCreateTimeDesc(requirementListRequestDto.getRequirementStatus(),patients,pageable).map(commonMapper::mapRequirementToRequirementResponseDto));

    }

    @Override
    public PageResponse<TestResultDto> getNegativePatientList(ListRequestDto listRequestDto, String username) {
        Pageable pageable = PageRequest.of(listRequestDto.getPageNum(),listRequestDto.getPageSize());
        List<Patient> patients = patientRepository.findAllByBvdcGroupIn(bvdcGroupRepository.findByDoctors(doctorRepository.findDoctorByUser_Username(username)));
        if (patients==null || patients.size()==0) {
            return new PageResponse<>();
        }
        List<Requirement> requirements = requirementRepository.findAllByStatusAndRequirementTypeAndPatientIn(RequirementStatus.DANG_THUC_HIEN,RequirementType.A_PATIENT_STATUS,patients);
        if (requirements!=null && requirements.size()!=0) {
            List<Patient> requiredPatientList = requirements.stream().map(Requirement::getPatient).collect(Collectors.toList());
            patients = patients.stream().filter(patient -> !requiredPatientList.contains(patient)).collect(Collectors.toList());
        }
        return PageResponse.buildPageResponse(testResultRepository.getNegativeResult(patients,29.0f,pageable).map(patientMapper::mapTestResultToTestResultDto));
    }

    @Override
    public PatientInfoDto changeGroupType(GroupType newGroupType, String patientCmnd, String username) {
        Doctor doctor = doctorRepository.findDoctorByUser_Username(username);
        Patient patient = patientRepository.findPatientByUser_Username(patientCmnd);
        if (patient==null) {
            throw new ApiException(ApiSharedMesssage.PATIENT_NOT_FOUND);
        }

        if (patient.getBvdcGroup()!=null) {

            List<BvdcGroup> bvdcGroupList = bvdcGroupRepository.findByDoctors(doctor);
            Optional<BvdcGroup> checkBelong = bvdcGroupList.stream().filter(bvdcGroup -> bvdcGroup.getId().equals(patient.getBvdcGroup().getId())).findAny();
            if (checkBelong.isPresent()) {
                Optional<BvdcGroup> checkExist = bvdcGroupList.stream().filter(bvdcGroup -> bvdcGroup.getGroupType().equals(newGroupType)).findAny();
                if (checkExist.isPresent()) {
                    patient.setBvdcGroup(checkExist.get());
                    NotificationDto notificationDto = new NotificationDto();
                    notificationDto.setTitle("Bệnh nhân " + patient.getUser().getName()+" ("+patient.getUser().getUsername()+") " + " vừa được thay đổi nhóm điều trị");
                    notificationDto.setDescription("Từ nhóm: " + (checkBelong.get().getGroupType()==GroupType.NGUY_CO?"Nguy cơ":checkBelong.get().getGroupType()==GroupType.NGUY_CO_THAP?"Nguy cơ thấp":"Nguy cơ cao") + " sang nhóm: "  + (checkExist.get().getGroupType()==GroupType.NGUY_CO?"Nguy cơ":checkExist.get().getGroupType()==GroupType.NGUY_CO_THAP?"Nguy cơ thấp":"Nguy cơ cao") );
                    notificationDto.setNotificationType(NotificationType.E_GROUP);
                    notificationService.addNotification(notificationDto,patient,false,true,true);
                    return patientMapper.mapPatientToPatientInfoResponseDto(patientRepository.save(patient));
                }
                else {
                    throw new ApiException(ApiSharedMesssage.GROUP_TYPE_NOT_HAVE);
                }
            }
        }

        throw new ApiException(ApiSharedMesssage.PATIENT_NOT_BELONG);

    }

    @Override
    public RequirementDto requestChangePatientStatus(PatientStatusDto patientStatusDto, String patientCmnd, String username) {
        RequirementDto requirementDto = new RequirementDto();
        requirementDto.setPatientCmnd(patientCmnd);
        requirementDto.setRequirementType(RequirementType.A_PATIENT_STATUS);
        requirementDto.setCreateTime(new Date());
        requirementDto.setAuto(false);
        requirementDto.setPatientStatusType(patientStatusDto.getPatientStatusType());
        return patientCommonService.addRequirement(requirementDto,patientCmnd);
    }

    @Override
    public void cancelXuatVien(String patientCmnd) {
        Patient patient = patientRepository.findPatientByUser_Username(patientCmnd);
        if (patient==null) {
            throw new ApiException(ApiSharedMesssage.PATIENT_NOT_FOUND);
        }
        patient.setPositiveDate(new Date());
        patientRepository.save(patient);
    }

    private PatientInfoDto examinationChangeGroupType(GroupType newGroupType, Patient patient, Doctor doctor) {

        if (patient.getBvdcGroup()!=null) {

            List<BvdcGroup> bvdcGroupList = bvdcGroupRepository.findByDoctors(doctor);
            Optional<BvdcGroup> checkBelong = bvdcGroupList.stream().filter(bvdcGroup -> bvdcGroup.getId().equals(patient.getBvdcGroup().getId())).findAny();
            if (checkBelong.isPresent()) {
                Optional<BvdcGroup> checkExist = bvdcGroupList.stream().filter(bvdcGroup -> bvdcGroup.getGroupType().equals(newGroupType)).findAny();
                if (checkExist.isPresent()) {
                    NotificationDto notificationDto = new NotificationDto();
                    notificationDto.setTitle("Bệnh nhân " + patient.getUser().getName() + " (" + patient.getUser().getUsername() + ") " + "vừa thay đổi nhóm điều trị");
                    notificationDto.setDescription("Bệnh nhân vừa được bác sĩ thay đổi sang nhóm: " + (newGroupType == GroupType.NGUY_CO_THAP ? "Nguy cơ thấp" : newGroupType == GroupType.NGUY_CO ? "Nguy cơ" : "Nguy cơ cao"));
                    notificationDto.setNotificationType(NotificationType.E_GROUP);
                    Map<String, String> pushData = new HashMap<>();
                    pushData.put("oldGroup", String.valueOf(patient.getBvdcGroup().getGroupType()));
                    pushData.put("newGroup", String.valueOf(newGroupType));
                    notificationService.addNotificationWithData(notificationDto, pushData, patient, true, true, true);
                    patient.setIsAutoGroup(false);
                    patient.setBvdcGroup(checkExist.get());
                    return patientMapper.mapPatientToPatientInfoResponseDto(patientRepository.save(patient));
                }
                else {
                    throw new ApiException(ApiSharedMesssage.GROUP_TYPE_NOT_HAVE);
                }
            }
        }

        throw new ApiException(ApiSharedMesssage.PATIENT_NOT_BELONG);

    }


}
