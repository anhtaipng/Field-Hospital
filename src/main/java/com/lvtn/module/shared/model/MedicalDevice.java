package com.lvtn.module.shared.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String medicalDeviceNumber;

    @ManyToOne
    @JoinColumn(name = "medicalDeviceType",referencedColumnName = "medicalDeviceType",nullable = false)
    private MedicalDeviceType medicalDeviceType;

    @JsonBackReference
    @ToString.Exclude
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="floor_no", referencedColumnName="floor_no"),
            @JoinColumn(name="building_name", referencedColumnName="building_name"),
            @JoinColumn(name="room_no", referencedColumnName="room_no"),
            @JoinColumn(name="sickbed_no", referencedColumnName="sickbed_no"),
    })
    private Sickbed sickbed;
}
