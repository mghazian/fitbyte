package com.coffeeteam.fitbyte.activitytype.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coffeeteam.fitbyte.core.entity.ActivityType;

public interface ActivityTypeRepository extends JpaRepository<ActivityType, Long> {
    Optional<ActivityType> findByName(String name);
}
