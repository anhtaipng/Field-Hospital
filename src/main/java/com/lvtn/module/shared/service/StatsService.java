package com.lvtn.module.shared.service;

import com.lvtn.module.shared.dto.*;
import com.lvtn.module.shared.enumeration.GroupType;
import com.lvtn.module.shared.model.StatisticResult;
import com.lvtn.module.shared.model.StatisticType;
import com.lvtn.platform.common.PageResponse;

import java.util.List;

public interface StatsService {
    List<StatisticResult> getStats(String username);
    StatisticResultResponse getStatsByType(StatisticResultRequest statisticResultRequest, String username);
    List<StatisticResult> getFirstStatsByType(String username);
    StatsResponseDto createStats(StatsCreateRequest statsCreateRequest,String username);
    void deleteStats(StatsResponseDto statsResponseDto);
}
