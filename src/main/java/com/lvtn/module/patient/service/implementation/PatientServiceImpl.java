package com.lvtn.module.patient.service.implementation;

import com.lvtn.module.patient.dto.PatientSignupDto;
import com.lvtn.module.patient.service.PatientService;
import com.lvtn.module.shared.common.ApiSharedMesssage;
import com.lvtn.module.shared.dto.*;
import com.lvtn.module.shared.enumeration.*;
import com.lvtn.module.shared.mapper.CommonMapper;
import com.lvtn.module.shared.mapper.PatientMapper;
import com.lvtn.module.shared.model.*;
import com.lvtn.module.shared.repository.*;
import com.lvtn.module.shared.service.NotificationService;
import com.lvtn.module.shared.service.PatientCommonService;
import com.lvtn.module.user.common.ApiClientMessage;
import com.lvtn.module.user.dto.PushNotificationRequest;
import com.lvtn.module.user.service.UserService;
import com.lvtn.module.user.service.implementation.PushNotificationService;
import com.lvtn.platform.common.PageResponse;
import com.lvtn.platform.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@Slf4j
public class PatientServiceImpl implements PatientService {
    @Autowired
    UserService userService;

    @Autowired
    PatientMapper patientMapper;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    PatientStatusRepository patientStatusRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RequirementRepository requirementRepository;

    @Autowired
    CommonMapper commonMapper;

    @Autowired
    PushNotificationService pushNotificationService;
    @Autowired
    BvdcGroupRepository bvdcGroupRepository;

    @Autowired
    PatientLocationRepository patientLocationRepository;
    @Autowired
    NotificationService notificationService;
    @Autowired
    OtpRepository otpRepository;
    @Autowired
    TestTypeRepository testTypeRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    PatientCommonService patientCommonService;
    @Autowired
    SickbedRepository sickbedRepository;

    @Override
    public PatientInfoDto signupPatient(PatientSignupDto patientSignupDto) {
        Otp existed;
        Optional<Otp> otp = otpRepository.findById(patientSignupDto.getCmnd());
        if (otp.isEmpty()) {
            throw new ApiException(ApiClientMessage.ACCOUNT_NOT_FOUND);
        } else {
            existed = otp.get();
        }
        if (new Date().after(existed.getOtpExpireTime())) {
            existed.setOtpCode(null);
            otpRepository.save(existed);
            throw new ApiException(ApiClientMessage.OTP_EXPIRED);
        }
        else if (passwordEncoder.matches(patientSignupDto.getOtpCode(),existed.getOtpCode())  && new Date().before(existed.getOtpExpireTime())) {
            existed.setOtpCode(null);
            otpRepository.save(existed);
            UserSignupDto userSignupDto = new UserSignupDto();
            BeanUtils.copyProperties(patientSignupDto,userSignupDto);
            User user = userService.signupUser(userSignupDto, "ROLE_PATIENT");
            Patient patient = Patient.builder().user(user).isAutoGroup(true).patientStatus(patientStatusRepository.getById(PatientStatusType.REQUEST_NAM_VIEN)).positiveDate(new Date()).hospitalizedDate(new Date()).build();
            patient.setCurrentPatientStatus(PatientStatusType.REQUEST_NAM_VIEN);
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
                patient = patientRepository.save(patient);
                NotificationDto notificationDto = new NotificationDto();
                notificationDto.setTitle("Bệnh nhân " + patientSignupDto.getName() + " (" + patientSignupDto.getCmnd() + ") " + "vừa vào nhóm điều trị");
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
                    patientCommonService.addRequirement(requirementDto, patientSignupDto.getCmnd());
                }
            } else {
                patient = patientRepository.save(patient);
            }


            Page<Sickbed> newSickbed =  sickbedRepository.findByRoom_RoomType_RoomTypeAndSickbedStatus("Thường", SickbedStatus.EMPTY, pageable);
            if (newSickbed.getContent().size() != 0) {
                RequirementDto requirementDto = new RequirementDto();
                requirementDto.setRequirementType(RequirementType.B_SICKBED);
                requirementDto.setPatientLocation(commonMapper.mapSickbedToPatientLocationResponseDto(newSickbed.getContent().get(0)));
                requirementDto.setStatus(RequirementStatus.DANG_THUC_HIEN);
                requirementDto.setCreateTime(new Date());
                requirementDto.setAuto(true);
                patientCommonService.addRequirement(requirementDto, patientSignupDto.getCmnd());
                NotificationDto notificationDto = new NotificationDto();
                notificationDto.setNotificationType(NotificationType.E_REQUIREMENT);
                notificationDto.setTitle("Bệnh nhân " + patient.getUser().getName() + " (" + patient.getUser().getUsername() + ") " + "có yêu cầu mới");
                notificationDto.setDescription("Xin vui lòng cập nhật chỉ số sức khỏe để hệ thống tự động phân vào nhóm phù hợp và cập nhật vị trí giường bệnh theo yêu cầu.");
                notificationService.addNotification(notificationDto, patient, true, false, false);
            } else {
                if (bvdcGroup!=null) {
                    for (Doctor doctor: bvdcGroup.getDoctors()){
                        patientCommonService.support(doctor.getUser().getUsername(), patient.getUser().getUsername());
                    }
                    NotificationDto notificationDto = new NotificationDto();
                    notificationDto.setNotificationType(NotificationType.E_REQUIREMENT);
                    notificationDto.setTitle("Bệnh nhân " + patient.getUser().getName() + " (" + patient.getUser().getUsername() + ") " + "có yêu cầu mới");
                    notificationDto.setDescription("Xin vui lòng cập nhật chỉ số sức khỏe để hệ thống tự động phân vào nhóm phù hợp và yêu cầu bác sĩ hoặc y tá hỗ trợ sắp xếp vị trí giường bệnh.");
                    notificationService.addNotification(notificationDto, patient, true, false, false);
                }
            }
            return patientMapper.mapPatientToPatientInfoResponseDto(patient);
        }
        else {
            throw new ApiException(ApiClientMessage.WRONG_OTP);
        }
    }

    @Override
    public PageResponse<RequirementDto> getRequirementList(RequirementListRequestDto requirementListRequestDto, String username) {
        Patient patient = patientRepository.findPatientByUser_Username(username);
        if (patient==null) {
            throw new ApiException(ApiSharedMesssage.PATIENT_NOT_FOUND);
        }
        Pageable pageable = PageRequest.of(requirementListRequestDto.getPageNum(),requirementListRequestDto.getPageSize());
        if (requirementListRequestDto.isAll()){
            return PageResponse.buildPageResponse(requirementRepository.findByStatusAndPatient_User_UsernameOrderByRequirementTypeAscCreateTimeAsc(RequirementStatus.DANG_THUC_HIEN,username,pageable).map(commonMapper::mapRequirementToRequirementResponseDto));
        }
        List<RequirementType> requirementTypeList = new ArrayList<>();
        requirementTypeList.add(RequirementType.B_SICKBED);
        requirementTypeList.add(RequirementType.D_STATISTIC);

        if (requirementListRequestDto.getRequirementStatus()==null && requirementListRequestDto.getRequirementType()==null) {
            return PageResponse.buildPageResponse(requirementRepository.findByStatusAndRequirementTypeInAndPatient_User_UsernameOrderByRequirementTypeAscCreateTimeAsc(RequirementStatus.DANG_THUC_HIEN,requirementTypeList,username,pageable).map(commonMapper::mapRequirementToRequirementResponseDto));
        } else if (requirementListRequestDto.getRequirementType()!=null) {
            return PageResponse.buildPageResponse(requirementRepository.findByStatusAndRequirementTypeAndPatient_User_UsernameOrderByCreateTimeAsc(RequirementStatus.DANG_THUC_HIEN,requirementListRequestDto.getRequirementType(),username,pageable).map(commonMapper::mapRequirementToRequirementResponseDto));
        }
        return PageResponse.buildPageResponse(requirementRepository.findByStatusAndRequirementTypeInAndPatient_User_UsernameOrderByRequirementTypeAscCreateTimeAsc(RequirementStatus.DANG_THUC_HIEN,requirementTypeList,username,pageable).map(commonMapper::mapRequirementToRequirementResponseDto));
    }


}
