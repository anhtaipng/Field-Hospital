package com.lvtn.module.shared.model;

import com.lvtn.module.shared.enumeration.RequirementStatus;
import com.lvtn.module.shared.enumeration.RequirementType;
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
public class MedicineRequirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RequirementStatus status;

    @Column(nullable = false)
    private String medicineName;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date requirementTime;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "doctor_id",referencedColumnName = "id",nullable = false)
    private Doctor doctor;

    private String quantity;

}
