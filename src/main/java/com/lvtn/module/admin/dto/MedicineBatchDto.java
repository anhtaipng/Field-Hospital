package com.lvtn.module.admin.dto;

import com.lvtn.module.shared.enumeration.GroupType;
import com.lvtn.module.shared.model.Medicine;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class MedicineBatchDto {
    private Long id;

    private Date inputDate;

    private Date expiredDate;

    private Long availableQuantity;

    private String medicineName;

    private String batchNumber;
}
