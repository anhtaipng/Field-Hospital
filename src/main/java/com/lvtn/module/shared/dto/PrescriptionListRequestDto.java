package com.lvtn.module.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrescriptionListRequestDto {
    private String keyword;
    private int pageSize;
    private int pageNum;
    private boolean check;
}
