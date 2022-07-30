package com.lvtn.module.shared.model;

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
public class Otp {
    @Id
    private String cmnd;

    private String otpCode;

    @Temporal(TemporalType.TIMESTAMP)
    private Date otpExpireTime;
}

