package com.lvtn.module.shared.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date inputDate;

    @Temporal(TemporalType.DATE)
    private Date expiredDate;

    private Long availableQuantity;

    @Column(unique = true, nullable = false)
    private String batchNumber;

    @ManyToOne
    @JoinColumn(name = "medicineName",referencedColumnName = "medicineName",nullable = false)
    private Medicine medicineName;
}
