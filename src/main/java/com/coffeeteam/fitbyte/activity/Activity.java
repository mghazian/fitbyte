package com.coffeeteam.fitbyte.activity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "activity_type_id")
    private ActivityType activityType;

    private LocalDateTime doneAt;
    private int durationInMinutes;
    private int caloriesBurned;
    
    public int getDurationInMinutes() {
        return durationInMinutes;
    }
    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }
    public LocalDateTime getDoneAt() {
        return doneAt;
    }
    public void setDoneAt(LocalDateTime doneAt) {
        this.doneAt = doneAt;
    }
    public ActivityType getActivityType() {
        return activityType;
    }
    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getCaloriesBurned() {
        return caloriesBurned;
    }
    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }
    
    
}
