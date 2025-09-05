package com.coffeeteam.fitbyte.activity.dto;

import com.coffeeteam.fitbyte.core.validator.IsoDate;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActivityUpdateRequestBody {
    
    private String activityType;

    @IsoDate()
    private String doneAt;

    @Min(1)
    private int durationInMinutes;
}
