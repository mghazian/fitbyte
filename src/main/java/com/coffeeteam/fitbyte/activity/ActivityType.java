package com.coffeeteam.fitbyte.activity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activity_types")
@NoArgsConstructor
public class ActivityType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int caloriesPerMinute;

    public int getCaloriesPerMinute() {
        return caloriesPerMinute;
    }
    public void setCaloriesPerMinute(int caloriesPerMinute) {
        this.caloriesPerMinute = caloriesPerMinute;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
