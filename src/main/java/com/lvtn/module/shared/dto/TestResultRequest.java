package com.lvtn.module.shared.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TestResultRequest {
    private int pageSize;
    private int pageNum;
    private String testType;
}
