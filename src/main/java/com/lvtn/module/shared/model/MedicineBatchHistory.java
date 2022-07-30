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
public class MedicineBatchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prescription_id",referencedColumnName = "id")
    private Prescription prescription;

    @Column(nullable = false)
    private int quantityDeducted;

    @Temporal(TemporalType.DATE)
    private Date timeCreated;

    @ManyToOne
    @JoinColumn(name = "medicine_batch_id",referencedColumnName = "id", nullable = false)
    private MedicineBatch medicineBatch;
}
