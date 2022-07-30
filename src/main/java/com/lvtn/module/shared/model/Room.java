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
@Builder
@IdClass(RoomPK.class)
@Table(name = "room")
public class Room {
    @Id
    @Column(length = 50, name = "room_no")
    private String roomNo;

    @ManyToOne
    @Id
    @JoinColumns({
            @JoinColumn(name="floor_no", referencedColumnName="floor_no"),
            @JoinColumn(name="building_name", referencedColumnName="building_name")
    })
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Floor floor;

    @ManyToOne
    @JoinColumn(name = "room_type", referencedColumnName = "roomType",nullable = false)
    private RoomType roomType;
}
