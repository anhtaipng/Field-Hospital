package com.lvtn.module.shared.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lvtn.module.shared.enumeration.RequirementType;
import com.lvtn.module.shared.enumeration.RequirementStatus;
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
public class Requirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RequirementStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date executionTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RequirementType requirementType;

    @ManyToOne
    @JoinColumn(name = "patient_id",referencedColumnName = "id",nullable = false)
    private Patient patient;

    private Boolean isAuto = false;

    @ManyToOne
    @JoinColumn(name = "requirement_hospital", referencedColumnName = "name")
    private CovidHospital covidHospital;

    @ManyToOne
    @JoinColumn(name = "requirement_statistic",referencedColumnName = "statisticType")
    private StatisticType statisticTypes;

    @ManyToOne
    @JoinColumn(name = "requirement_test",referencedColumnName = "testType")
    private TestType testType;

    @ManyToOne
    @JoinColumn(name = "requirement_status",referencedColumnName = "patientStatusType")
    private PatientStatus patientStatus;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="floor_no", referencedColumnName="floor_no"),
            @JoinColumn(name="building_name", referencedColumnName="building_name"),
            @JoinColumn(name="room_no", referencedColumnName="room_no"),
            @JoinColumn(name="sickbed_no", referencedColumnName="sickbed_no"),
    })
    private Sickbed sickbed;
}
