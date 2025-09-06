package com.coffeeteam.fitbyte.activity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityGetResponse {
    private String activityId;
    private String activityType;
    private String doneAt;
    private int durationInMinutes;
    private int caloriesBurned;
    private String createdAt;
}
