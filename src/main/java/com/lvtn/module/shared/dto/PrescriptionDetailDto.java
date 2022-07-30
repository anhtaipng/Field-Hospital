package com.lvtn.module.shared.dto;


import com.lvtn.module.shared.model.Medicine;
import lombok.Data;

import java.util.Date;

@Data
public class PrescriptionDetailDto {

    private Medicine medicine;

    private Date fromDay;

    private Date toDay;

    private String amount;

    private Integer quantity;

    private String note;

}
