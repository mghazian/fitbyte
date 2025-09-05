package com.coffeeteam.fitbyte.activity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activities")
@NoArgsConstructor
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "activity_type_id")
    private ActivityType activityType;

    @Column(name = "doneat")
    private LocalDateTime doneAt;

    @Column(name = "durationinminutes")
    private int durationInMinutes;

    @Column(name = "calories_burned")
    private int caloriesBurned;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Activity(ActivityType activityType, LocalDateTime doneAt, int durationInMinutes, int caloriesBurned) {
        this.activityType = activityType;
        this.doneAt = doneAt;
        this.durationInMinutes = durationInMinutes;
        this.caloriesBurned = caloriesBurned;
    }

    public Activity setPatch(
        ActivityType activityType,
        LocalDateTime doneAt,
        int durationInMinutes,
        int caloriesBurned
    ) {
        this.activityType = activityType;
        this.doneAt = doneAt;
        this.durationInMinutes = durationInMinutes;
        this.caloriesBurned = caloriesBurned;
        return this;
    }
    
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
