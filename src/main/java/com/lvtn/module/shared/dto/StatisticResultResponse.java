package com.lvtn.module.shared.dto;

import com.lvtn.platform.common.PageResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticResultResponse {
    PageResponse<StatsResponseDto> result;
    PageResponse<StatsResponseDto> truongResult;
}
