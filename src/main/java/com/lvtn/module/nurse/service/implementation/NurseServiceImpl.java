package com.lvtn.module.nurse.service.implementation;

import com.google.common.base.Strings;
import com.lvtn.module.nurse.service.NurseService;
import com.lvtn.module.shared.common.ApiSharedMesssage;
import com.lvtn.module.shared.dto.*;
import com.lvtn.module.shared.enumeration.*;
import com.lvtn.module.shared.mapper.CommonMapper;
import com.lvtn.module.shared.mapper.PatientMapper;
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
public class NurseServiceImpl implements NurseService {

    @Autowired
    NurseRepository nurseRepository;
    @Autowired
    BvdcGroupRepository bvdcGroupRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    PatientMapper patientMapper;
    @Autowired
    RequirementRepository requirementRepository;
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    PatientStatusRepository patientStatusRepository;
    @Autowired
    NotificationService notificationService;
    @Autowired
    PrescriptionRepository prescriptionRepository;
    @Autowired
    MedicineBatchHistoryRepository medicineBatchHistoryRepository;
    @Autowired
    SickbedRepository sickbedRepository;
    @Autowired
    PatientLocationRepository patientLocationRepository;
    @Autowired
    TestTypeRepository testTypeRepository;
    @Autowired
    PatientCommonService patientCommonService;

    @Override
    public PageResponse<PatientInfoDto> getPatientList(PatientListRequestDto patientListRequestDto, String username) {
        Page<Patient> patients;
        Pageable pageable = PageRequest.of(patientListRequestDto.getPageNum(), patientListRequestDto.getPageSize());

        if (Strings.isNullOrEmpty(patientListRequestDto.getKeyword())) {
            try {
                patients = patientRepository.findByBvdcGroup(bvdcGroupRepository.findByGroupTypeAndNurses(GroupType.valueOf(patientListRequestDto.getGroupType()),nurseRepository.findNurseByUser_Username(username)),pageable);
            }
            catch (Exception e){
                patients = patientRepository.findAllByBvdcGroupInOrderById(bvdcGroupRepository.findByNurses(nurseRepository.findNurseByUser_Username(username)),pageable);
            }
        } else {
            if (patientListRequestDto.getKeyword().charAt(0)>='0' && patientListRequestDto.getKeyword().charAt(0)<='9') {
                try {
                    patients = patientRepository.findByBvdcGroupAndUser_UsernameContaining(bvdcGroupRepository.findByGroupTypeAndNurses(GroupType.valueOf(patientListRequestDto.getGroupType()),nurseRepository.findNurseByUser_Username(username)),patientListRequestDto.getKeyword(),pageable);
                }
                catch (Exception e){
                    patients = patientRepository.findAllByBvdcGroupInAndUser_UsernameContaining(bvdcGroupRepository.findByNurses(nurseRepository.findNurseByUser_Username(username)),patientListRequestDto.getKeyword(),pageable);
                }
            }
            else {
                try {
                    patients = patientRepository.findByBvdcGroupAndUser_NameContaining(bvdcGroupRepository.findByGroupTypeAndNurses(GroupType.valueOf(patientListRequestDto.getGroupType()),nurseRepository.findNurseByUser_Username(username)),patientListRequestDto.getKeyword(),pageable);
                }
                catch (Exception e){
                    patients = patientRepository.findAllByBvdcGroupInAndUser_NameContaining(bvdcGroupRepository.findByNurses(nurseRepository.findNurseByUser_Username(username)),patientListRequestDto.getKeyword(),pageable);
                }
            }
        }
        return PageResponse.buildPageResponse(patients.map(patientMapper::mapPatientToPatientInfoResponseDto));
    }

    @Override
    public PageResponse<RequirementDto> getRequirementList(RequirementListRequestDto requirementListRequestDto, String username) {

        Nurse nurse = nurseRepository.findNurseByUser_Username(username);

        List<Patient> patientList = patientRepository.findAllByBvdcGroupIn(bvdcGroupRepository.findByNurses(nurse));

        if (patientList.size()==0) {
            return new PageResponse<>();
        }

        List<RequirementType> requirementTypeList = new ArrayList<>();
        requirementTypeList.add(RequirementType.B_SICKBED);
        requirementTypeList.add(RequirementType.D_STATISTIC);
        requirementTypeList.add(RequirementType.C_TEST);

        Pageable pageable = PageRequest.of(requirementListRequestDto.getPageNum(),requirementListRequestDto.getPageSize());
        if (requirementListRequestDto.getRequirementStatus()==null && requirementListRequestDto.getRequirementType()==null) {
            return PageResponse.buildPageResponse(requirementRepository.findAllByStatusAndRequirementTypeInAndPatientInOrderByRequirementTypeAscCreateTimeAsc(RequirementStatus.DANG_THUC_HIEN,requirementTypeList,patientList,pageable).map(commonMapper::mapRequirementToRequirementResponseDto));
        } else if (requirementListRequestDto.getRequirementType()!=null) {
            return PageResponse.buildPageResponse(requirementRepository.findAllByStatusAndRequirementTypeAndPatientInOrderByRequirementTypeAscCreateTimeAsc(RequirementStatus.DANG_THUC_HIEN,requirementListRequestDto.getRequirementType(),patientList,pageable).map(commonMapper::mapRequirementToRequirementResponseDto));
        }

        return PageResponse.buildPageResponse(requirementRepository.findAllByStatusAndRequirementTypeInAndPatientInOrderByRequirementTypeAscCreateTimeAsc(RequirementStatus.DANG_THUC_HIEN,requirementTypeList,patientList,pageable).map(commonMapper::mapRequirementToRequirementResponseDto));

    }

    @Override
    public PatientInfoDto changePatientStatus(PatientStatusDto patientStatusDto, String patientCmnd) {
        Patient patient = patientRepository.findPatientByUser_Username(patientCmnd);
        if (patient==null) {
            throw new ApiException(ApiSharedMesssage.PATIENT_NOT_FOUND);
        }

        Optional<PatientStatus> patientStatus = patientStatusRepository.findById(patientStatusDto.getPatientStatusType());
        if (patientStatus.isEmpty()) {
            throw new ApiException(55111,"Trạng thái mới cho bệnh nhân không tồn tại");
        }
        if (patientStatusDto.getPatientStatusType().equals(PatientStatusType.CHUYEN_VIEN)) {
            if (patientStatusDto.getCovidHospital() != null) {
                patient.setToCovidHospital(patientStatusDto.getCovidHospital());
            } else {
                throw new ApiException(55111,"Không tìm thấy bệnh viện tiếp nhận");
            }
        }
        else if (patientStatusDto.getPatientStatusType().equals(PatientStatusType.NAM_VIEN)) {
            patient.setIsAutoGroup(true);
            patient.setHospitalizedDate(new Date());
            patient.setDischargeDate(null);
            patient.setToCovidHospital(null);
            patient.setPositiveDate(new Date());
            Pageable pageable = PageRequest.of(0, 1);
            BvdcGroup bvdcGroup = null;
            Page<Doctor> doctorList = bvdcGroupRepository.selectGroup(pageable);
            if (doctorList.getContent().size() != 0) {
                bvdcGroup = bvdcGroupRepository.findByGroupTypeAndDoctors(GroupType.NGUY_CO_THAP,doctorList.getContent().get(0));
                if (bvdcGroup == null) {
                    bvdcGroup = bvdcGroupRepository.findByGroupTypeAndDoctors(GroupType.NGUY_CO,doctorList.getContent().get(0));
                }
                if (bvdcGroup == null) {
                    bvdcGroup = bvdcGroupRepository.findByGroupTypeAndDoctors(GroupType.NGUY_CO_CAO,doctorList.getContent().get(0));
                }
            }
            if (bvdcGroup != null) {
                patient.setBvdcGroup(bvdcGroup);
                NotificationDto notificationDto = new NotificationDto();
                notificationDto.setTitle("Bệnh nhân " + patient.getUser().getName() + " (" + patient.getUser().getUsername() + ") " + "vừa vào nhóm điều trị");
                notificationDto.setDescription("Bệnh nhân vừa được hệ thống tự động xếp vào nhóm: " + (bvdcGroup.getGroupType() == GroupType.NGUY_CO_THAP ? "Nguy cơ thấp" : bvdcGroup.getGroupType() == GroupType.NGUY_CO ? "Nguy cơ" : "Nguy cơ cao"));
                notificationDto.setNotificationType(NotificationType.E_GROUP);
                Map<String, String> pushData = new HashMap<>();
                pushData.put("newGroup", String.valueOf(bvdcGroup.getGroupType()));
                pushData.put("oldGroup", "");
                notificationService.addNotificationWithData(notificationDto, pushData, patient, true, true, true);

                List<TestType> testTypeList = testTypeRepository.findAll();
                if (!testTypeList.isEmpty()) {
                    RequirementDto requirementDto = new RequirementDto();
                    requirementDto.setRequirementType(RequirementType.C_TEST);
                    requirementDto.setTestType(testTypeList.get(0).getTestType());
                    requirementDto.setStatus(RequirementStatus.DANG_THUC_HIEN);
                    requirementDto.setCreateTime(new Date(patient.getHospitalizedDate().getTime() + 1000 * 60 * 60 * 24 * 7));
                    requirementDto.setAuto(true);
                    patientCommonService.addRequirement(requirementDto, patient.getUser().getUsername());
                }
            }

            NotificationDto notificationDto = new NotificationDto();
            notificationDto.setNotificationType(NotificationType.E_REQUIREMENT);
            notificationDto.setTitle("Bệnh nhân " + patient.getUser().getName()+" ("+patient.getUser().getUsername()+") " + "có yêu cầu mới");
            notificationDto.setDescription("Xin vui lòng cập nhật chỉ số sức khỏe để hệ thống tự động phân vào nhóm phù hợp và cập nhật vị trí giường bệnh theo sự hướng dẫn của y tá.");
            notificationService.addNotification(notificationDto,patient,true,false,false);
        }
        patient.setPatientStatus(patientStatus.get());
        patient.setCurrentPatientStatus(patientStatusDto.getPatientStatusType());

        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setNotificationType(NotificationType.A_PATIENT_STATUS);
        notificationDto.setTitle("Bệnh nhân " + patient.getUser().getName() + " ("+patientCmnd+")" + " vừa thay đổi trạng thái");
        notificationDto.setDescription("Bệnh nhân hiện tại đã "+ (patientStatusDto.getPatientStatusType()==PatientStatusType.CHUYEN_VIEN?"chuyển viện đến " + patientStatusDto.getCovidHospital():patientStatusDto.getPatientStatusType()==PatientStatusType.TU_VONG?"tử vong":patientStatusDto.getPatientStatusType()==PatientStatusType.XUAT_VIEN?"xuất viện":"nằm viện trở lại"));
        notificationService.addNotification(notificationDto,patient,true,false,true);

        if (patientStatusDto.getPatientStatusType()!=PatientStatusType.NAM_VIEN) {
            List<Requirement> requirements = requirementRepository.findByStatusAndPatient_User_Username(RequirementStatus.DANG_THUC_HIEN,patientCmnd);
            if (requirements!=null && !requirements.isEmpty()) {
                for (Requirement requirement : requirements) {
                    if (requirement.getRequirementType().equals(RequirementType.A_PATIENT_STATUS)) {
                        if (requirement.getPatientStatus().getPatientStatusType().equals(patientStatusDto.getPatientStatusType())) {
                            requirement.setStatus(RequirementStatus.HOAN_THANH);
                            requirement.setExecutionTime(new Date());
                            requirementRepository.save(requirement);
                        } else {
                            requirement.setStatus(RequirementStatus.HUY_BO);
                            requirementRepository.save(requirement);
                        }
                    } else {
                        requirement.setStatus(RequirementStatus.HUY_BO);
                        requirementRepository.save(requirement);
                    }
                }
            }


            PatientLocation oldPatientLocation = patientLocationRepository.findByPatient_User_UsernameAndEndTimeIsNull(patientCmnd);
            if (oldPatientLocation!=null) {
                oldPatientLocation.setEndTime(new Date());
                patientLocationRepository.save(oldPatientLocation);
                PatientLocationDto oldSickbed = commonMapper.mapPatientLocationToPatientLocationResponseDto(oldPatientLocation);
                sickbedRepository.updateSickbedStatus(String.valueOf(SickbedStatus.EMPTY),oldSickbed.getSickbedNo(),oldSickbed.getRoomNo(),
                        oldSickbed.getFloorNo(),oldSickbed.getBuildingName());
            }
            patient.setBvdcGroup(null);
            patient.setDischargeDate(new Date());
        }

        return patientMapper.mapPatientToPatientInfoResponseDto(patientRepository.save(patient));
    }

    @Override
    public PageResponse<PrescriptionResponseDto> getPrescription(PrescriptionListRequestDto prescriptionListRequestDto, String username) {
        Pageable pageable = PageRequest.of(prescriptionListRequestDto.getPageNum(), prescriptionListRequestDto.getPageSize());

        List<BvdcGroup> bvdcGroupList = bvdcGroupRepository.findByNurses(nurseRepository.findNurseByUser_Username(username));

        if (bvdcGroupList==null || bvdcGroupList.isEmpty()) {
            throw new ApiException(1244,"Bạn không phụ trách nhóm bệnh nhân nào");
        }

        Page<Prescription> prescriptions;
        if (prescriptionListRequestDto.getKeyword()!=null && !prescriptionListRequestDto.getKeyword().equals("")) {
            if (prescriptionListRequestDto.getKeyword().charAt(0)>='0' && prescriptionListRequestDto.getKeyword().charAt(0)<='9') {
                prescriptions = prescriptionRepository.findAllByPatient_BvdcGroupInAndTookAndPatient_User_UsernameContainingOrderByCreateTimeAsc(bvdcGroupList, prescriptionListRequestDto.isCheck(),prescriptionListRequestDto.getKeyword(),pageable);
            }
            else {
                prescriptions = prescriptionRepository.findAllByPatient_BvdcGroupInAndTookAndPatient_User_NameContainingOrderByCreateTimeAsc(bvdcGroupList,prescriptionListRequestDto.isCheck(),prescriptionListRequestDto.getKeyword(),pageable);
            }
        } else {
            prescriptions = prescriptionRepository.findAllByPatient_BvdcGroupInAndTookOrderByCreateTimeAsc(bvdcGroupList,prescriptionListRequestDto.isCheck(),pageable);
        }
        return PageResponse.buildPageResponse(prescriptions.map(commonMapper::mapPrescriptionToPrescriptionResponseDto));
    }



    @Override
    public PrescriptionDto getMedicineBatchHistory(Long id) {
        Optional<Prescription> prescription = prescriptionRepository.findById(id);
        if (prescription.isEmpty()) {
            throw new ApiException(451421,"Đơn thuốc không tồn tại");
        }
        List<MedicineBatchHistory> medicineBatchHistory = medicineBatchHistoryRepository.findByPrescription(prescription.get());
        return commonMapper.mapPrescriptionBatchToPrescriptionDto(prescription.get(),medicineBatchHistory);
    }

    @Override
    public void checkPrescription(boolean check, Long id) {
        Optional<Prescription> prescription = prescriptionRepository.findById(id);
        if (prescription.isEmpty()) {
            throw new ApiException(451421,"Đơn thuốc không tồn tại");
        }
        Prescription new_prescription = prescription.get();
        new_prescription.setTook(check);
        prescriptionRepository.save(new_prescription);
    }
}
