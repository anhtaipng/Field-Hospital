package com.lvtn.module.shared.dto;

import com.lvtn.module.shared.enumeration.RequirementStatus;
import com.lvtn.module.shared.enumeration.RequirementType;
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
public class RequirementListRequestDto {
    private boolean all;
    private RequirementStatus requirementStatus;
    private RequirementType requirementType;
    private int pageSize;
    private int pageNum;
}
