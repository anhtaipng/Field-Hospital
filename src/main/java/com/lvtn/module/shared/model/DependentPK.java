package com.lvtn.module.shared.model;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class DependentPK implements Serializable {
    private String name;
    private Long patient;
}
