package com.lvtn.module.shared.dto;

import com.lvtn.module.admin.dto.MedicineBatchDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class PrescriptionDto {

    private Long prescriptionId;

    private boolean check;

    private Date timeCreated;

    List<MedicineBatchHistoryDto> medicineBatchHistoryList;
}
