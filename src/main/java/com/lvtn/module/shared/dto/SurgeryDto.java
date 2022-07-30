package com.lvtn.module.shared.dto;

import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
public class SurgeryDto {
    private Long id;
    private String surgeryResult;
    private String complication;
    private String bodyPart;
    private Date surgeryTime;
    private String surgeryPlace;
}
