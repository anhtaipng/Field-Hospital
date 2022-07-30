package com.lvtn.module.admin.dto;

import com.lvtn.module.shared.enumeration.GroupType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BvdcGroupResponseDto {
    private Long id;
    private GroupType groupType;
    private List<String> doctors;
    private List<String> doctorsNames;
    private List<String> nurses;
    private List<String> nursesNames;
    private int sumPatient;
}
