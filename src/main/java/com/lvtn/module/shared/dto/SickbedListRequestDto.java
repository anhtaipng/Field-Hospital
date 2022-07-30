package com.lvtn.module.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SickbedListRequestDto {
    private String roomType;
    private String search;
    private int pageSize;
    private int pageNum;
}
