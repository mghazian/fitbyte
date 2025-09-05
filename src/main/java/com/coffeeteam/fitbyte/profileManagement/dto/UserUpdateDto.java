package com.coffeeteam.fitbyte.profileManagement.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class UserUpdateDto {

    @Pattern(regexp = "CARDIO|WEIGHT", message = "Preference must be CARDIO or WEIGHT")
    @NotBlank
    private String preference;

    @NotBlank
    @Pattern(regexp = "KG|LBS", message = "Weight unit must be KG or LBS")
    private String weightUnit;

    @NotBlank
    @Pattern(regexp = "CM|INCH", message = "Height unit must be CM or INCH")
    private String heightUnit;

    @NotNull
    @Min(value = 10, message = "Weight must be at least 10")
    @Max(value = 1000, message = "Weight must not exceed 1000")
    private Integer weight;

    @NotNull
    @Min(value = 3, message = "Height must be at least 3")
    @Max(value = 250, message = "Height must not exceed 250")
    private Integer height;

    @NotBlank
    @Size(min = 2, max = 60, message = "Name must be between 2 and 60 characters")
    @Pattern(regexp = "^(?!(true|false|yes|no|1|0)$).*",
            message = "Name cannot be a boolean value")
    @Pattern(regexp = "^[a-zA-Z\\s'.]+$",
            message = "Nama hanya boleh mengandung huruf, spasi, apostrof, dan titik")
    private String name;


    @NotNull
    @NotEmpty
    @Pattern(regexp = "^(https?|ftp)://[a-zA-Z0-9]([a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(\\.([a-zA-Z0-9]([a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?))*\\.[a-zA-Z]{2,}(:[0-9]{1,5})?(/.*)?$",
            message = "Must be a complete and valid URL with proper domain")
    private String imageUri;
}