package com.lvtn.module.admin.dto;

import lombok.Data;

import java.util.List;

@Data
public class CountTestInAWeekDto {
    String testTypes;
    Long day0, day1, day2, day3, day4, day5, day6, day7;
}
