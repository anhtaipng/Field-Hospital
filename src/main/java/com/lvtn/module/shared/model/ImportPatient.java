package com.lvtn.module.shared.model;

import com.lvtn.module.shared.enumeration.Gender;
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
public class ImportPatient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cmnd;
    private String name;
    private String phone;
    private String email;
    private String password;
    private String tinh;
    private String huyen;
    private String xa;
    private String thon;
    private String bhyt;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Temporal(TemporalType.DATE)
    private Date birthDay;

    private String sicknesses;

    private boolean isAdded;

}
