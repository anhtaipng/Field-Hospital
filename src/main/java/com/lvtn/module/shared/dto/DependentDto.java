package com.lvtn.module.shared.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class DependentDto {
    private String name;
    @Pattern(regexp = "^[0-9]*$",message = "Điện thoại chỉ bao gồm các ký tự số")
    private String phone;
    private String email;
    private String relationShip;
}
