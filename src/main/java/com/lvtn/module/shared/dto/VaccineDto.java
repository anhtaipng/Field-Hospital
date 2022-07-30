package com.lvtn.module.shared.dto;

import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
public class VaccineDto {
    private Long id;
    private String vaccineType;
    @Temporal(TemporalType.TIMESTAMP)
    private Date injectionTime;
    private String injectionPlace;
    private Integer noInjection;
    private String brand;

}
