package com.lvtn.module.user.service.implementation;

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
import com.lvtn.module.user.dto.ImportPatientDto;
import com.lvtn.module.user.dto.PushNotificationRequest;
import com.lvtn.module.user.dto.UserDto;
import com.lvtn.module.user.mapper.UserMapper;
import com.lvtn.module.user.service.UserService;
import com.lvtn.platform.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    PatientCommonService patientCommonService;

    @Autowired
    PatientMapper patientMapper;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    PatientStatusRepository patientStatusRepository;


    @Autowired
    RequirementRepository requirementRepository;

    @Autowired
    CommonMapper commonMapper;

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
    ImportPatientRepository importPatientRepository;
    @Autowired
    SickbedRepository sickbedRepository;

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final PushNotificationService pushNotificationService;

    @Override
    public User signupUser(UserSignupDto userSignupDto, String roleName) {
        User existed = userRepository.findUserByUsername(userSignupDto.getCmnd());
        if (existed != null) {
            throw new ApiException(ApiClientMessage.ACCOUNT_EXISTED);
        }
        if (userSignupDto.getCmnd().trim().length()!=9 && userSignupDto.getCmnd().trim().length()!=12) {
            throw new ApiException(123111,"CMND phải có 9 ký tự hoặc CCCD phải có 12 ký tự");
        }
        User user = User.builder().password(passwordEncoder.encode(userSignupDto.getPassword())).username(userSignupDto.getCmnd())
                .phone(userSignupDto.getPhone())
                .name(userSignupDto.getName())
                .authorities(List.of(authorityRepository.findAuthorityByRoleName(roleName)))
                .enable(true).build();

        return user;
    }

    @Override
    public User changePassword(UserChangePasswordDto userChangePasswordDto, Principal user) {
        User existed = userRepository.findUserByUsername(user.getName());
        if (existed == null) {
            throw new ApiException(ApiClientMessage.ACCOUNT_NOT_FOUND);
        };
        User new_user = userRepository.findUserByUsername(user.getName());
        new_user.setPassword(passwordEncoder.encode(userChangePasswordDto.getNewPassword()));
        return userRepository.save(new_user);
    }


    @Override
    public void resetPassword(ResetPasswordDto info) {
        User existed = userRepository.findUserByUsername(info.getCmnd());
        if (existed == null) {
            throw new ApiException(123123,"Chứng minh nhân dân không tồn tại");
        }

        if (!existed.getPhone().equals(info.getPhoneNumber())) {
            throw new ApiException(ApiClientMessage.WRONG_PHONE_NUMBER);
        }
        if (new Date().after(existed.getOtpExpireTime())) {
            existed.setOtpCode(null);
            userRepository.save(existed);
            throw new ApiException(ApiClientMessage.OTP_EXPIRED);
        }
        else if (passwordEncoder.matches(info.getOtp(),existed.getOtpCode()) && new Date().before(existed.getOtpExpireTime())) {
            existed.setPassword(passwordEncoder.encode(info.getNewPassword()));
            existed.setOtpCode(null);
            userRepository.save(existed);
        } else {
            throw new ApiException(ApiClientMessage.WRONG_OTP);
        }

    }

    @Override
    public User getUser(String username) {
        User existed = userRepository.findUserByUsername(username);
        if (existed == null) {
            throw new ApiException(ApiClientMessage.ACCOUNT_NOT_FOUND);
        };
        return existed;
    }

    @Override
    public UserDto updateUserProfile(UserDto userDto, String username) {
        User existed = userRepository.findUserByUsername(username);
        if (existed == null) {
            throw new UsernameNotFoundException("Username not found with username: " + userDto.getUsername());
        }
        if (!isStringEmpty(userDto.getName())) {
            existed.setName(userDto.getName());
        }
        if (!isStringEmpty(userDto.getEmail())) {
            existed.setEmail(userDto.getEmail());
        }
        if (!isStringEmpty(userDto.getBirthDate().toString())) {
            existed.setBirthDate(userDto.getBirthDate());
        }
        if (!isStringEmpty(userDto.getGender().toString())) {
            existed.setGender(userDto.getGender());
        }
        if (!isStringEmpty(userDto.getPhone())) {
            existed.setPhone(userDto.getPhone());
        }

        return mapper.mapUserToUserDto(existed);
    }

    @Override
    public UserDto loadUserProfileByUsername(String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new ApiException(ApiClientMessage.ACCOUNT_NOT_FOUND);
        }
        return mapper.mapUserToUserDto(user);
    }

    @Override
    public Authority saveAuthority(Authority authority) {
        return authorityRepository.save(authority);
    }

    @Override
    public User updateFcmToken(String fcmToken, String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new ApiException(ApiClientMessage.ACCOUNT_NOT_FOUND);
        }
        if (user.getFcmToken()!=null && !user.getFcmToken().equals(fcmToken)) {
            PushNotificationRequest pushNotificationRequest = new PushNotificationRequest();
            pushNotificationRequest.setTitle("Tài khoản đã được đăng nhập từ thiết bị khác.");
            pushNotificationRequest.setToken(user.getFcmToken());
            pushNotificationRequest.setMessage("");
            pushNotificationService.sendPushNotificationToToken(pushNotificationRequest);
        }

        user.setFcmToken(fcmToken);
        return user;
    }

    @Override
    public User removeFcmToken(String fcmToken, String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new ApiException(ApiClientMessage.ACCOUNT_NOT_FOUND);
        }
        if (user.getFcmToken() != null && user.getFcmToken().equals(fcmToken)) {
            user.setFcmToken(null);
        }
        return user;
    }

    @Override
    public void importPatient(ImportPatientDto importPatientDto) {
        Optional<ImportPatient> importPatient = importPatientRepository.findById(importPatientDto.getId());
        if (importPatient.isEmpty()){
            throw new ApiException(ApiSharedMesssage.IMPORT_PATIENT_NOT_FOUND);
        }
        PatientSignupDto patientSignupDto = new PatientSignupDto();
        BeanUtils.copyProperties(importPatientDto, patientSignupDto);
        patientSignupDto.setPassword(importPatientDto.getCmnd());
        UserSignupDto userSignupDto = new UserSignupDto();
        BeanUtils.copyProperties(patientSignupDto,userSignupDto);
        User user = signupUser(userSignupDto, "ROLE_PATIENT");
        user.setBirthDate(importPatientDto.getBirthDay());
        user.setEmail(importPatientDto.getEmail());
        user.setGender(importPatientDto.getGender());
        Patient patient = Patient.builder().user(user).isAutoGroup(true).patientStatus(patientStatusRepository.getById(PatientStatusType.REQUEST_NAM_VIEN)).positiveDate(new Date()).hospitalizedDate(new Date()).build();
        patient.setTinh(importPatientDto.getTinh() + "=79");
        patient.setHuyen(importPatientDto.getHuyen() + "=766");
        patient.setXa(importPatientDto.getXa() + "=27004");
        patient.setThon(importPatientDto.getThon());
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

        for (String sickne: importPatientDto.getSickness()) {
            if (sickne!=null && !sickne.trim().equals("")) {
                SicknessDto sicknessDto = new SicknessDto();
                sicknessDto.setSicknessType(sickne);
                patientCommonService.addSickness(sicknessDto, user.getUsername());
            }
        }
        ImportPatient importPatient1 = importPatient.get();
        importPatient1.setAdded(true);
        importPatientRepository.save(importPatient1);

    }

    @Override
    public void addImportPatient(ImportPatientDto importPatientDto) {
        importPatientRepository.save(mapper.mapImportPatientDtoToImportPatient(importPatientDto));
    }

    @Override
    public void deleteImportPatient(Long idImportPatient) {
        Optional<ImportPatient> importPatient = importPatientRepository.findById(idImportPatient);
        if (importPatient.isEmpty()){
            throw new ApiException(ApiSharedMesssage.IMPORT_PATIENT_NOT_FOUND);
        }
        importPatientRepository.delete(importPatient.get());
    }

    @Override
    public List<ImportPatientDto> listImportPatientIsReady() {
        return importPatientRepository.findByIsAddedIsFalse().stream()
                .map(mapper::mapImportPatientToImportPatientDto).collect(Collectors.toList());
    }

    private boolean isStringEmpty(String s) {
        return s == null || s.isEmpty() || s.isBlank();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username not found with username: " + username);
        }
        return user;
    }
}
