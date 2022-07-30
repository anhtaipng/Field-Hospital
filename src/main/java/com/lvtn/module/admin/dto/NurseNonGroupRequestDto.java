package com.lvtn.module.admin.dto;

import com.lvtn.module.shared.enumeration.GroupType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NurseNonGroupRequestDto {
    String shift;
    GroupType groupType;
}
