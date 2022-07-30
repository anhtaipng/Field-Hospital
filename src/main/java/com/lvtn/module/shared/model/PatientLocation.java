package com.lvtn.module.shared.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class PatientLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="floor_no", referencedColumnName="floor_no", nullable = false),
            @JoinColumn(name="building_name", referencedColumnName="building_name", nullable = false),
            @JoinColumn(name="room_no", referencedColumnName="room_no", nullable = false),
            @JoinColumn(name="sickbed_no", referencedColumnName="sickbed_no", nullable = false),
    })
    private Sickbed sickbed;

    @ManyToOne
    @JoinColumn(name = "patient_id",referencedColumnName = "id")
    private Patient patient;
}