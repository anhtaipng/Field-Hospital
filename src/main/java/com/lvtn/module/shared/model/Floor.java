package com.lvtn.module.shared.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(FloorPK.class)
@Builder
public class Floor {
    @Id
    @Column(length = 50, name = "floor_no")
    private String floorNo;

    @ManyToOne
    @Id
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "building_name",referencedColumnName = "building_name")
    private Building building;
}
