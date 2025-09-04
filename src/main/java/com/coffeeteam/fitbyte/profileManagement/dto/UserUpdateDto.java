package com.coffeeteam.fitbyte.profileManagement.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserUpdateDto {

    @Pattern(regexp = "CARDIO|WEIGHT", message = "Preference must be CARDIO or WEIGHT")
    private String preference;

    @Pattern(regexp = "KG|LBS", message = "Weight unit must be KG or LBS")
    private String weightUnit;

    @Pattern(regexp = "CM|INCH", message = "Height unit must be CM or INCH")
    private String heightUnit;

    @Min(value = 10, message = "Weight must be at least 10")
    @Max(value = 1000, message = "Weight must not exceed 1000")
    private Integer weight;

    @Min(value = 3, message = "Height must be at least 3")
    @Max(value = 250, message = "Height must not exceed 250")
    private Integer height;

    @Size(min = 2, max = 60, message = "Name must be between 2 and 60 characters")
    private String name;

    private String imageUri;
}