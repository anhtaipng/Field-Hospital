package com.lvtn.module.shared.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lvtn.module.shared.enumeration.SickbedStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(SickbedPK.class)
@Builder
@Table(name = "sickbed")
public class Sickbed {
    @Id
    @Column(length = 50,name = "sickbed_no")
    private String sickBedNo;

    @ManyToOne
    @Id
    @JoinColumns({
            @JoinColumn(name="floor_no", referencedColumnName="floor_no"),
            @JoinColumn(name="building_name", referencedColumnName="building_name"),
            @JoinColumn(name="room_no", referencedColumnName="room_no")
    })
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Room room;

    @Column(nullable = false,name = "sickbed_status")
    @Enumerated(EnumType.STRING)
    private SickbedStatus sickbedStatus;

    @JsonManagedReference
    @OneToMany(mappedBy = "sickbed", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<MedicalDevice> medicalDevices;
}
