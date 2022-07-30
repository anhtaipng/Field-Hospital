package com.lvtn.module.shared.mapper;

import com.lvtn.module.shared.dto.AllStatsResponseDto;
import com.lvtn.module.shared.dto.StatsCreateRequest;
import com.lvtn.module.shared.dto.StatsResponseDto;
import com.lvtn.module.shared.enumeration.GroupType;
import com.lvtn.module.shared.model.StatisticResult;
import com.lvtn.module.shared.model.StatisticType;
import com.lvtn.module.shared.repository.PatientRepository;
import com.lvtn.module.shared.repository.StatisticTypeRepository;
import com.lvtn.module.shared.service.StatsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class StatsMapper {
    @Autowired
    PatientRepository patientRepository;

    @Autowired
    StatisticTypeRepository statisticTypeRepository;



    public StatisticResult mapStatsCreateRequestToStatisticResult(StatsCreateRequest statsCreateRequest){
        StatisticResult statisticResult = new StatisticResult();
        if (statsCreateRequest.getTimeCreated()!=null) {
            statisticResult.setTimeCreated(statsCreateRequest.getTimeCreated());
        } else {
            statisticResult.setTimeCreated(new Date());
        }
        statisticResult.setValue(statsCreateRequest.getValue());
        statisticResult.setStatisticType(statisticTypeRepository.getById(statsCreateRequest.getStatisticType()));
        return statisticResult;
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

    public StatsResponseDto mapStatisticResultToStatsResponseDto(StatisticResult statisticResult){
        StatsResponseDto statsResponseDto = new StatsResponseDto();
        statsResponseDto.setGroupType(null);
        statsResponseDto.setId(statisticResult.getId());
        statsResponseDto.setValue(statisticResult.getValue());
        statsResponseDto.setTimeCreated(statisticResult.getTimeCreated());
        statsResponseDto.setStatisticType(statisticResult.getStatisticType().getStatisticType());
        statsResponseDto.setPatientCmnd(statisticResult.getPatient().getUser().getUsername());
        return statsResponseDto;
    }

    public List<AllStatsResponseDto> mapStatisticResultToAllStatsResponseDto(List<StatisticResult> statisticResultList){
        StatisticResult thuResult = new StatisticResult();
        StatisticResult truongResult = new StatisticResult();
        List<AllStatsResponseDto> allStatsResponseDtoList = new ArrayList<>();
        for (StatisticResult statisticResult : statisticResultList) {
            if (statisticResult.getStatisticType().getStatisticType().equals("Huyết áp tâm thu")) {
                BeanUtils.copyProperties(statisticResult,thuResult);
            } else if (statisticResult.getStatisticType().getStatisticType().equals("Huyết áp tâm trương")) {
                 BeanUtils.copyProperties(statisticResult,truongResult);
            } else {
                AllStatsResponseDto allStatsResponseDto = new AllStatsResponseDto();
                allStatsResponseDto.setId(statisticResult.getId());
                allStatsResponseDto.setValue(statisticResult.getValue());
                allStatsResponseDto.setTimeCreated(statisticResult.getTimeCreated());
                allStatsResponseDto.setStatisticType(statisticResult.getStatisticType());
                if (statisticResult.getPatient() != null) {
                    allStatsResponseDto.setGroupType(getGroupTypeFromResult(statisticResult,null));
                    allStatsResponseDto.setPatientCmnd(statisticResult.getPatient().getUser().getUsername());
                }
                allStatsResponseDtoList.add(allStatsResponseDto);
            }
        }
        AllStatsResponseDto allStatsResponseDto = new AllStatsResponseDto();
        allStatsResponseDto.setId(truongResult.getId());
        allStatsResponseDto.setValue(truongResult.getValue());
        allStatsResponseDto.setTimeCreated(truongResult.getTimeCreated());
        allStatsResponseDto.setStatisticType(thuResult.getStatisticType());
        if (truongResult.getPatient() != null) {
            allStatsResponseDto.setGroupType(getGroupTypeFromResult(truongResult,thuResult.getValue()));
            allStatsResponseDto.setPatientCmnd(truongResult.getPatient().getUser().getUsername());
        }
        allStatsResponseDto.setTruongType(truongResult.getStatisticType());
        allStatsResponseDto.setThuValue(thuResult.getValue());
        allStatsResponseDtoList.add(allStatsResponseDto);
        return allStatsResponseDtoList;
    }

}
