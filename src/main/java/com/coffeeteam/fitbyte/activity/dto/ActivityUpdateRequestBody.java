package com.coffeeteam.fitbyte.activity.dto;

import com.coffeeteam.fitbyte.core.validator.isodate.IsoDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
