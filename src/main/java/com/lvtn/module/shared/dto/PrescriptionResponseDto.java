package com.lvtn.module.shared.dto;


import com.lvtn.module.shared.model.Medicine;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PrescriptionResponseDto {

    private Long id;

    private Date timeCreated;

    private String doctorName;

    private String patientName;

    private String doctorCmnd;

    private String patientCmnd;

    List<PrescriptionDetailDto> prescriptionList;

    private String diagnostic;

    private String note;

    private boolean check;

}
