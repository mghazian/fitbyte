package com.coffeeteam.fitbyte.activity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityTypeRepository extends JpaRepository<ActivityType, Long> {
    Optional<ActivityType> findByName(String name);
}
