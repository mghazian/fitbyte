package com.coffeeteam.fitbyte.activity.dto;

import com.coffeeteam.fitbyte.core.validator.IsoDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActivityUpdateRequestBody {
    
    @NotNull
    @NotEmpty
    private String activityType;

    @IsoDate()
    private String doneAt;

    @Min(1)
    private Integer durationInMinutes;
}
