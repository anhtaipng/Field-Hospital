package com.lvtn.module.shared.service.implementation;

import com.lvtn.module.doctor.service.DoctorService;
import com.lvtn.module.patient.common.ApiPatientMesssage;
import com.lvtn.module.shared.common.ApiSharedMesssage;
import com.lvtn.module.shared.dto.*;
import com.lvtn.module.shared.enumeration.*;
import com.lvtn.module.shared.mapper.StatsMapper;
import com.lvtn.module.shared.model.*;
import com.lvtn.module.shared.repository.*;
import com.lvtn.module.shared.service.NotificationService;
import com.lvtn.module.shared.service.PatientCommonService;
import com.lvtn.module.shared.service.StatsService;
import com.lvtn.platform.common.PageResponse;
import com.lvtn.platform.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final PatientRepository patientRepository;
    private final StatisticResultRepository statisticResultRepository;
    private final StatisticTypeRepository statisticTypeRepository;
    private final NotificationService notificationService;
    private final StatsMapper statsMapper;
    private final RequirementRepository requirementRepository;
    private final BvdcGroupRepository bvdcGroupRepository;
    private final TestTypeRepository testTypeRepository;
    private final PatientCommonService patientCommonService;

    @Override
    public List<StatisticResult> getStats(String username){
        Patient patient = patientRepository.findPatientByUser_Username(username);
        if (patient == null) {
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        return statisticResultRepository.findAllByPatient_User_Username(username);
    }

    @Override
    public StatisticResultResponse getStatsByType(StatisticResultRequest statisticResultRequest, String username) {
        Patient patient = patientRepository.findPatientByUser_Username(username);
        if (patient == null) {
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        Pageable pageable = PageRequest.of(statisticResultRequest.getPageNum(),statisticResultRequest.getPageSize());

        if (statisticResultRequest.getStatisticType().equals("Huyết áp")) {
            Page<StatisticResult> tamThuResult = statisticResultRepository.findByStatisticType_StatisticTypeAndPatient_User_UsernameOrderByTimeCreatedDesc("Huyết áp tâm thu",username,pageable);
            Page<StatisticResult> tamTruongResult = statisticResultRepository.findByStatisticType_StatisticTypeAndPatient_User_UsernameOrderByTimeCreatedDesc("Huyết áp tâm trương",username,pageable);
            return new StatisticResultResponse(PageResponse.buildPageResponse(tamThuResult.map(statsMapper::mapStatisticResultToStatsResponseDto)),PageResponse.buildPageResponse(tamTruongResult.map(statsMapper::mapStatisticResultToStatsResponseDto)));
        } else {
            Optional<StatisticType> st = statisticTypeRepository.findById(statisticResultRequest.getStatisticType());
            if (st.isEmpty()) {
                throw new ApiException(ApiSharedMesssage.STATISTIC_TYPE_NOT_FOUND);
            }
            return new StatisticResultResponse(PageResponse.buildPageResponse(statisticResultRepository.findByStatisticType_StatisticTypeAndPatient_User_UsernameOrderByTimeCreatedDesc(statisticResultRequest.getStatisticType(),username,pageable).map(statsMapper::mapStatisticResultToStatsResponseDto)),null);
        }

    }


    @Override
    public List<StatisticResult> getFirstStatsByType(String username) {
        Patient patient = patientRepository.findPatientByUser_Username(username);
        if (patient == null) {
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        List<StatisticType> statisticTypes = statisticTypeRepository.findAll();
        List<StatisticResult> statisticResults = new ArrayList<>();
        if (!statisticTypes.isEmpty()) {
            for (StatisticType st:statisticTypes) {
                StatisticResult statisticResult = statisticResultRepository.findFirstByStatisticType_StatisticTypeAndPatient_User_UsernameOrderByTimeCreatedDesc(st.getStatisticType(),username);
                if (statisticResult != null){
                     statisticResults.add(statisticResult);
                } else {
                    statisticResult = new StatisticResult();
                    statisticResult.setStatisticType(st);
                    statisticResults.add(statisticResult);
                }
            }
        }
        return statisticResults;
    }

    public GroupType getGroupTypeFromResult(StatisticResult statisticResult,Float thuValue) {
        GroupType groupType = GroupType.NGUY_CO_CAO;
        if (!statisticResult.getStatisticType().getStatisticType().equals("Huyết áp tâm trương")) {
            if (statisticResult.getStatisticType().getStatisticType().equals("SPO2") && statisticResult.getValue()>99) {
                groupType = GroupType.NGUY_CO_THAP;
            } else if (statisticResult.getStatisticType().getMinValue() != null && statisticResult.getStatisticType().getLowValue() != null && statisticResult.getValue() >= statisticResult.getStatisticType().getMinValue() && statisticResult.getValue() < statisticResult.getStatisticType().getLowValue()) {
                groupType = GroupType.NGUY_CO;
            } else if (statisticResult.getStatisticType().getHighValue() != null && statisticResult.getStatisticType().getLowValue() != null && statisticResult.getValue() >= statisticResult.getStatisticType().getLowValue() && statisticResult.getValue() < statisticResult.getStatisticType().getHighValue()) {
                groupType = GroupType.NGUY_CO_THAP;
            } else if (statisticResult.getStatisticType().getHighValue() != null && statisticResult.getStatisticType().getMaxValue() != null && statisticResult.getValue() >= statisticResult.getStatisticType().getHighValue() && statisticResult.getValue() < statisticResult.getStatisticType().getMaxValue()) {
                groupType = GroupType.NGUY_CO;
            }
        } else {
            StatisticType tamThu = statisticTypeRepository.getById("Huyết áp tâm thu");
            StatisticType tamTruong = statisticTypeRepository.getById("Huyết áp tâm trương");
            if (tamThu.getLowValue()!=null && tamThu.getHighValue()!=null && tamThu.getLowValue() <= thuValue && tamThu.getHighValue() > thuValue) {
                groupType = GroupType.NGUY_CO_THAP;
            } else if (tamThu.getMaxValue()!=null && tamThu.getHighValue()!=null && tamThu.getHighValue() <= thuValue && tamThu.getMaxValue() > thuValue) {
                groupType = GroupType.NGUY_CO;
            } else if (tamThu.getMinValue()!=null && tamThu.getLowValue()!=null && tamThu.getMinValue() <= thuValue && tamThu.getLowValue() > thuValue) {
                groupType = GroupType.NGUY_CO;
            }

            if (groupType == GroupType.NGUY_CO_CAO) {
                return groupType;
            }

            GroupType truongGroupType = GroupType.NGUY_CO_CAO;

            if (tamTruong.getLowValue()!=null && tamTruong.getHighValue()!=null && tamTruong.getLowValue()<=statisticResult.getValue() && tamTruong.getHighValue()>statisticResult.getValue()) {
                truongGroupType = GroupType.NGUY_CO_THAP;
            } else if (tamTruong.getMaxValue()!=null && tamTruong.getHighValue()!=null && tamTruong.getHighValue()<=statisticResult.getValue() && tamTruong.getMaxValue()>statisticResult.getValue()) {
                truongGroupType = GroupType.NGUY_CO;
            } else if (tamTruong.getMinValue()!=null && tamTruong.getLowValue()!=null && tamTruong.getMinValue()<=statisticResult.getValue() && tamTruong.getLowValue()>statisticResult.getValue()) {
                truongGroupType = GroupType.NGUY_CO;
            }

            if (truongGroupType==GroupType.NGUY_CO_CAO) {
                return truongGroupType;
            }

            if (truongGroupType==GroupType.NGUY_CO || groupType==GroupType.NGUY_CO) {
                return GroupType.NGUY_CO;
            }

            return GroupType.NGUY_CO_THAP;
        }
        return groupType;
    }


    private GroupType getNewGroupType(Patient patient,StatisticResult statisticResult, Float thuValue) {

        List<StatisticType> statisticTypes = statisticTypeRepository.findAll();
        List<StatisticResult> statisticResults = new ArrayList<>();
        if (!statisticTypes.isEmpty()) {
            for (StatisticType st:statisticTypes) {
                StatisticResult oldStatisticResult = statisticResultRepository.findFirstByStatisticType_StatisticTypeAndPatient_User_UsernameOrderByTimeCreatedDesc(st.getStatisticType(),patient.getUser().getUsername());
                if (oldStatisticResult != null){
                    statisticResults.add(oldStatisticResult);
                }
            }
        }
        GroupType newGroupType = getGroupTypeFromResult(statisticResult, thuValue);
        if (newGroupType==GroupType.NGUY_CO_CAO) {
            return newGroupType;
        }
        if (statisticResults.size()>0) {
            if (statisticResult.getStatisticType().getStatisticType().equals("Huyết áp tâm trương")) {
                for (StatisticResult result : statisticResults) {
                    if (!result.getStatisticType().getStatisticType().equals(statisticResult.getStatisticType().getStatisticType()) && !result.getStatisticType().getStatisticType().equals("Huyết áp tâm thu")) {
                        GroupType currentGroupType = getGroupTypeFromResult(result, null);
                        if (currentGroupType == GroupType.NGUY_CO_CAO) {
                            return GroupType.NGUY_CO_CAO;
                        } else if (newGroupType == GroupType.NGUY_CO_THAP && currentGroupType == GroupType.NGUY_CO) {
                            newGroupType = GroupType.NGUY_CO;
                        }
                    }
                }
            } else {
                Float oldThuValue = 0f;
                StatisticResult oldTruongResult = null;
                for (StatisticResult result : statisticResults) {
                    if (result.getStatisticType().getStatisticType().equals("Huyết áp tâm thu")) {
                        oldThuValue = result.getValue();
                    } else if (result.getStatisticType().getStatisticType().equals("Huyết áp tâm trương")) {
                        oldTruongResult = result;
                    } else {
                        if (!result.getStatisticType().getStatisticType().equals(statisticResult.getStatisticType().getStatisticType())) {
                            GroupType currentGroupType = getGroupTypeFromResult(result, null);
                            if (currentGroupType == GroupType.NGUY_CO_CAO) {
                                return GroupType.NGUY_CO_CAO;
                            } else if (newGroupType == GroupType.NGUY_CO_THAP && currentGroupType == GroupType.NGUY_CO) {
                                newGroupType = GroupType.NGUY_CO;
                            }
                        }
                    }
                }
                if (oldTruongResult != null) {
                    GroupType currentGroupType = getGroupTypeFromResult(oldTruongResult, oldThuValue);
                    if (currentGroupType == GroupType.NGUY_CO_CAO) {
                        return GroupType.NGUY_CO_CAO;
                    } else if (newGroupType == GroupType.NGUY_CO_THAP && currentGroupType == GroupType.NGUY_CO) {
                        newGroupType = GroupType.NGUY_CO;
                    }
                }
            }
        }

        return newGroupType;

    }

    @Override
    public StatsResponseDto createStats(StatsCreateRequest statsCreateRequest, String username) {
        boolean isHuyetAp = false;
        if (statsCreateRequest.getValue()!=null && statsCreateRequest.getValue()<0) {
            throw new ApiException(123123,"Giá trị chỉ số sức khỏe không được bé hơn 0");
        }
        if (statsCreateRequest.getThuValue()!=null && statsCreateRequest.getThuValue()<0) {
            throw new ApiException(123123,"Giá trị chỉ số sức khỏe không được bé hơn 0");
        }
        if (statsCreateRequest.getStatisticType().equals("SPO2") && statsCreateRequest.getValue()>100) {
            throw new ApiException(123123,"Giá trị chỉ số SPO2 không thể lớn hơn 100");
        }

        Patient patient = patientRepository.findPatientByUser_Username(username);
        if (patient == null) {
            throw new ApiException(ApiPatientMesssage.PATIENT_NOT_FOUND);
        }
        if (statsCreateRequest.getStatisticType().equals("Huyết áp")) {
            isHuyetAp = true;
            StatsCreateRequest tamThuRequest = StatsCreateRequest.builder().value(statsCreateRequest.getThuValue())
                    .statisticType("Huyết áp tâm thu")
                    .timeCreated(statsCreateRequest.getTimeCreated())
                    .build();
            StatisticResult tamThuResult = statsMapper.mapStatsCreateRequestToStatisticResult(tamThuRequest);
            tamThuResult.setPatient(patient);
            statisticResultRepository.save(tamThuResult);
            statsCreateRequest.setStatisticType("Huyết áp tâm trương");
        } else {
            Optional<StatisticType> statisticType = statisticTypeRepository.findById(statsCreateRequest.getStatisticType());
            if (statisticType.isEmpty()){
                throw new ApiException(ApiSharedMesssage.STATISTIC_TYPE_NOT_FOUND);
            }
        }
        StatisticResult statisticResult = statsMapper.mapStatsCreateRequestToStatisticResult(statsCreateRequest);
        statisticResult.setPatient(patient);
        StatisticResult newStatisticResult = statisticResultRepository.save(statisticResult);
        if (patient.getPatientStatus().getPatientStatusType().equals(PatientStatusType.NAM_VIEN)) {

            List<Requirement> requirements = requirementRepository.findByStatusAndRequirementTypeAndPatient_User_UsernameOrderByCreateTimeDesc(RequirementStatus.DANG_THUC_HIEN, RequirementType.D_STATISTIC, username);
            if (requirements != null && !requirements.isEmpty()) {
                for (Requirement requirement : requirements) {
                    if (isHuyetAp) {
                        if (requirement.getStatisticTypes().getStatisticType().equals("Huyết áp tâm thu")) {
                            requirement.setExecutionTime(new Date());
                            requirement.setStatus(RequirementStatus.HOAN_THANH);
                            requirementRepository.save(requirement);
                        }
                    } else {
                        if (requirement.getStatisticTypes().getStatisticType().equals(statsCreateRequest.getStatisticType())) {
                            requirement.setExecutionTime(new Date());
                            requirement.setStatus(RequirementStatus.HOAN_THANH);
                            requirementRepository.save(requirement);
                        }
                    }
                }
            }
            GroupType newGroupType;
            if (!isHuyetAp) {
                newGroupType = getNewGroupType(patient, statisticResult, null);
            } else {
                newGroupType = getNewGroupType(patient, statisticResult, statsCreateRequest.getThuValue());
            }
            if (newGroupType != GroupType.NGUY_CO_THAP && patient.getPatientStatus().getPatientStatusType().equals(PatientStatusType.NAM_VIEN)) {
                if (isHuyetAp) {
                    NotificationDto notificationDto = new NotificationDto();
                    notificationDto.setTitle("Bệnh nhân " + patient.getUser().getName() + " (" + username + ") " + "vừa cập nhật chỉ số");
                    notificationDto.setDescription("Loại chỉ số: Huyết áp" + "\nGiá trị huyết áp tâm thu: " + statsCreateRequest.getThuValue() + ". Giá trị huyết áp tâm trương: " + statsCreateRequest.getValue()+"\nNhóm: " +newGroupType);
                    notificationDto.setCmnd(username);
                    notificationDto.setNotificationType(NotificationType.D_STATISTIC);
                    notificationService.addNotification(notificationDto, patient, false, true, true);
                } else {
                    NotificationDto notificationDto = new NotificationDto();
                    notificationDto.setTitle("Bệnh nhân " + patient.getUser().getName() + " (" + username + ") " + "vừa cập nhật chỉ số");
                    notificationDto.setDescription("Loại chỉ số: " + newStatisticResult.getStatisticType().getStatisticType() + " - Giá trị: " + statisticResult.getValue()+"\nNhóm: " +newGroupType);
                    notificationDto.setCmnd(username);
                    notificationDto.setNotificationType(NotificationType.D_STATISTIC);
                    notificationService.addNotification(notificationDto, patient, false, true, true);
                }
            }
            if (patient.getBvdcGroup()==null) {
                Pageable pageable = PageRequest.of(0, 1);
                Page<Doctor> doctorList = bvdcGroupRepository.selectGroup(pageable);
                BvdcGroup bvdcGroup = null;
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
                }
            }
            else if (patient.getBvdcGroup()!=null && patient.getIsAutoGroup() && newGroupType != patient.getBvdcGroup().getGroupType()) {
                List<BvdcGroup> bvdcGroupList = bvdcGroupRepository.findByDoctors(patient.getBvdcGroup().getDoctors().get(0));
                Optional<BvdcGroup> checkBelong = bvdcGroupList.stream().filter(bvdcGroup -> bvdcGroup.getId().equals(patient.getBvdcGroup().getId())).findAny();
                if (checkBelong.isPresent()) {
                    Optional<BvdcGroup> checkExist = bvdcGroupList.stream().filter(bvdcGroup -> bvdcGroup.getGroupType().equals(newGroupType)).findAny();
                    if (checkExist.isPresent()) {
                        NotificationDto notificationDto = new NotificationDto();
                        notificationDto.setTitle("Bệnh nhân " + patient.getUser().getName() + " (" + username + ") " + "vừa thay đổi nhóm điều trị");
                        notificationDto.setDescription("Bệnh nhân vừa được hệ thống tự động thay đổi sang nhóm: " + (newGroupType == GroupType.NGUY_CO_THAP ? "Nguy cơ thấp" : newGroupType == GroupType.NGUY_CO ? "Nguy cơ" : "Nguy cơ cao"));
                        notificationDto.setNotificationType(NotificationType.E_GROUP);
                        Map<String, String> pushData = new HashMap<>();
                        pushData.put("oldGroup", String.valueOf(patient.getBvdcGroup().getGroupType()));
                        pushData.put("newGroup", String.valueOf(newGroupType));
                        notificationService.addNotificationWithData(notificationDto, pushData, patient, true, true, true);
                        patient.setBvdcGroup(checkExist.get());
                    }
                }
            }
        }
        return statsMapper.mapStatisticResultToStatsResponseDto(newStatisticResult);
    }

    @Override
    public void deleteStats(StatsResponseDto statsResponseDto) {
        Optional<StatisticResult> statisticResult = statisticResultRepository.findById(statsResponseDto.getId());
        if (statisticResult.isEmpty()) {
            throw new ApiException(ApiSharedMesssage.STATISTIC_RESULT__NOT_FOUND);
        }
        statisticResultRepository.delete(statisticResult.get());
    }

}
