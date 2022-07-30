package com.lvtn.module.shared.dto;

import lombok.Data;

@Data
public class StatisticResultRequest {
    private int pageSize;
    private int pageNum;
    private String statisticType;
}
