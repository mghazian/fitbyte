package com.coffeeteam.fitbyte.profileManagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {
    private String preference;
    private String weightUnit;
    private String heightUnit;
    private Integer weight;
    private Integer height;
    private String email;
    private String name;
    private String imageUri;
}