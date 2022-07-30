package com.lvtn.module.shared.dto;

import com.lvtn.module.admin.dto.MedicineBatchDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class MedicineBatchHistoryDto {

    private Long id;

    private int quantityDeducted;

    private Date timeCreated;

    private MedicineBatchDto medicineBatch;
}
