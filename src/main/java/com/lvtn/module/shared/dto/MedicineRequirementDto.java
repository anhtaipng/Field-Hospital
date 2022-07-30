package com.lvtn.module.shared.dto;


import com.lvtn.module.shared.enumeration.RequirementStatus;
import com.lvtn.module.shared.model.Doctor;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Data
public class MedicineRequirementDto {
    private Long id;

    private RequirementStatus status;

    private String medicineName;

    private Date requirementTime;

    private String doctorCMND;

    private String quantity;
}
