package com.coffeeteam.fitbyte.activity.dto;

public class ActivityPersistResponse {
    private String activityId;
    private String activityType;
    private String doneAt;
    private int durationInMinutes;
    private int caloriesBurned;
    private String createdAt;
    private String updatedAt;

    public ActivityPersistResponse(String activityId, String activityType, String doneAt, int durationInMinutes,
            int caloriesBurned, String createdAt, String updatedAt) {
        this.activityId = activityId;
        this.activityType = activityType;
        this.doneAt = doneAt;
        this.durationInMinutes = durationInMinutes;
        this.caloriesBurned = caloriesBurned;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getActivityId() {
        return activityId;
    }
    public void setActivityId(String activityId) {
        this.activityId = activityId;
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
    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public String getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    public int getCaloriesBurned() {
        return caloriesBurned;
    }
    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }
    
    
}
