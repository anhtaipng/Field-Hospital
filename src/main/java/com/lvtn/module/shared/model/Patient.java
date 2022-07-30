package com.lvtn.module.shared.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lvtn.module.shared.enumeration.PatientStatusType;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bhyt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cmnd",referencedColumnName = "username",unique = true,nullable = false)
    private User user;

    private String toCovidHospital;

    private String tinh;
    private String huyen;
    private String xa;
    private String thon;

    @Temporal(TemporalType.DATE)
    private Date hospitalizedDate;
    @Temporal(TemporalType.DATE)
    private Date dischargeDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date positiveDate;
    private Boolean isAutoGroup = true;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "patientStatus",referencedColumnName = "patientStatusType",nullable = false)
    private PatientStatus patientStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PatientStatusType currentPatientStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    @JoinColumn(name = "group_id",referencedColumnName = "id")
    private BvdcGroup bvdcGroup;

    @JsonIgnoreProperties("patient")
    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "patient")
    private List<Dependent> dependents;

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Patient))
            return false;
        Patient f = (Patient) o;
        return f.user.getUsername().equals(user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user.getUsername());
    }
    
}
