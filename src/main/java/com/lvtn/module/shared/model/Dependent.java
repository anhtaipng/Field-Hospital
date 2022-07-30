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

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@IdClass(DependentPK.class)
@Builder
public class Dependent{
    @Id
    private String name;
    private String phone;
    private String email;
    private String relationShip;

    @ManyToOne
    @Id
    @JsonIgnoreProperties("dependents")
    @JoinColumn(name = "patient_id",referencedColumnName = "id",nullable = false)
    private Patient patient;
}
