package com.coffeeteam.fitbyte.activity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coffeeteam.fitbyte.core.entity.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
}
