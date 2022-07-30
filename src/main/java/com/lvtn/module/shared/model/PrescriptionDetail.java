package com.lvtn.module.shared.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(PrescriptionDetailPK.class)
public class PrescriptionDetail {

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medicineName",referencedColumnName = "medicineName",nullable = false)
    private Medicine medicine;

    @Id
    @JsonIgnoreProperties("patient")
    @ManyToOne
    @JoinColumn(name = "prescription_id",referencedColumnName = "id",nullable = false)
    private Prescription prescription;

    @Temporal(TemporalType.DATE)
    private Date fromDay;

    @Temporal(TemporalType.DATE)
    private Date toDay;

    private String amount;

    private Integer quantity;

    private String note;
}
