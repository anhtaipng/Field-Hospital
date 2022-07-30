package com.lvtn.module.shared.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class PrescriptionDetailPK implements Serializable {
    private Long prescription;
    private String medicine;
}
