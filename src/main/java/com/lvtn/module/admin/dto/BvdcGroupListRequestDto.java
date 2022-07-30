package com.lvtn.module.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BvdcGroupListRequestDto {
    private int pageSize;
    private int pageNum;
}
