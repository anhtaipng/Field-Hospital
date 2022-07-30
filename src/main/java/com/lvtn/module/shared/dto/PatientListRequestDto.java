package com.lvtn.module.shared.dto;

import com.lvtn.module.shared.enumeration.GroupType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientListRequestDto {
    private String groupType;
    private String keyword;
    private int pageSize;
    private int pageNum;
}
