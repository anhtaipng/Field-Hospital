package com.lvtn.module.admin.dto;

import lombok.Data;

@Data
public class SickbedDashboardDto {
    Long empty, used, requested, disable, waiting;
}
