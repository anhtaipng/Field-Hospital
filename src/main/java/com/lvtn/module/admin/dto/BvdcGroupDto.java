package com.lvtn.module.admin.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lvtn.module.shared.enumeration.GroupType;
import com.lvtn.module.shared.model.Doctor;
import com.lvtn.module.shared.model.Nurse;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class BvdcGroupDto {
    private Long id;

    private GroupType groupType;

    private List<String> doctors;

    private List<String> nurses;
}
