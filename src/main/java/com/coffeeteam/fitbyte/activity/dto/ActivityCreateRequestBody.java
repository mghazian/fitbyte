package com.coffeeteam.fitbyte.activity.dto;

import com.coffeeteam.fitbyte.core.validator.IsoDate;

import jakarta.validation.constraints.NotEmpty;

public class ActivityCreateRequestBody {

    @NotEmpty()
    private String activityType;

    @NotEmpty()
    @IsoDate()
    private String doneAt;
    
    @NotEmpty()
    private int durationInMinutes;
    
    public ActivityCreateRequestBody(String activityType, String doneAt, int durationInMinutes) {
        this.activityType = activityType;
        this.doneAt = doneAt;
        this.durationInMinutes = durationInMinutes;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getDoneAt() {
        return doneAt;
    }

    public void setDoneAt(String doneAt) {
        this.doneAt = doneAt;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    
    
}
