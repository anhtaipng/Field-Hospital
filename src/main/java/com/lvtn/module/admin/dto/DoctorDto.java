package com.lvtn.module.admin.dto;

import com.lvtn.module.shared.model.BvdcGroup;
import com.lvtn.module.shared.model.User;
import lombok.Data;

import java.util.List;

@Data
public class DoctorDto {
    private Long id;
    private User user;
    private List<BvdcGroup> bvdcGroups;
    private String shift;
}
