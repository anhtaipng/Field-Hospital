package com.lvtn.module.shared.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lvtn.module.shared.enumeration.PatientStatusType;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientStatus {

    @Id
    @Enumerated(EnumType.STRING)
    private PatientStatusType patientStatusType;
}
