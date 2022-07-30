package com.lvtn.module.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExaminationListRequestDto {
    private boolean isSearch;
    private Date fromDate;
    private Date toDate;
    private int pageSize;
    private int pageNum;
}
