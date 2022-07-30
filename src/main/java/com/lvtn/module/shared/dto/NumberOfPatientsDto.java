package com.lvtn.module.shared.dto;

import com.lvtn.module.shared.enumeration.GroupType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NumberOfPatientsDto {
    private long count;
    private GroupType groupType;
}
