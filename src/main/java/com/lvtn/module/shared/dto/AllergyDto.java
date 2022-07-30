package com.lvtn.module.shared.dto;

import lombok.Data;

@Data
public class AllergyDto {
    private Long id;
    private String allergyGroup;
    private String allergyType;
    private String description;
    private String expression;
}
