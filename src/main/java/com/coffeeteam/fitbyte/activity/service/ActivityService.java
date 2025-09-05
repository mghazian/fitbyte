package com.coffeeteam.fitbyte.activity.service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coffeeteam.fitbyte.activity.dto.ActivityPersistResponse;
import com.coffeeteam.fitbyte.activity.dto.ActivityUpdateRequestBody;
import com.coffeeteam.fitbyte.activity.exceptions.ActivityNotFoundException;
import com.coffeeteam.fitbyte.activity.exceptions.ActivityTypeNotFoundException;
import com.coffeeteam.fitbyte.activity.Activity;
import com.coffeeteam.fitbyte.activity.ActivityRepository;
import com.coffeeteam.fitbyte.activity.ActivityType;
import com.coffeeteam.fitbyte.activity.ActivityTypeRepository;
import com.coffeeteam.fitbyte.activity.dto.ActivityCreateRequestBody;
import com.coffeeteam.fitbyte.activity.dto.ActivityGetResponse;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class ActivityService {
    
    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityTypeRepository activityTypeRepository;

    @Autowired
    private EntityManager entityManager;

    public ActivityPersistResponse createActivity(
        ActivityCreateRequestBody activityRequestBody
    ) throws ActivityTypeNotFoundException {
        ActivityType correspondingType = activityTypeRepository.findByName(activityRequestBody.getActivityType()).orElseThrow(() -> new ActivityTypeNotFoundException());
        int caloriesBurned = correspondingType.getCaloriesPerMinute() * activityRequestBody.getDurationInMinutes();
        OffsetDateTime doneAt = OffsetDateTime.parse(activityRequestBody.getDoneAt());
        
        Activity saved = activityRepository.save(new Activity(
            correspondingType,
            doneAt,
            activityRequestBody.getDurationInMinutes(),
            caloriesBurned
        ));
        return mapToPersistResponse(saved);
    }

    public List<ActivityGetResponse> findActivities(
        int limit,
        int offset,
        String activityType,
        String doneAtFromString,
        String doneAtToString,
        int caloriesBurnedMin,
        int caloriesBurnedMax
    ) {
        OffsetDateTime doneAtFrom = null;
        if (doneAtFromString != null && !doneAtFromString.isEmpty()) {
            try {
                doneAtFrom = OffsetDateTime.parse(doneAtFromString);
            } catch (DateTimeParseException e) {
                doneAtFrom = null;
            }
        }

        OffsetDateTime doneAtTo = null;
        if (doneAtToString != null && !doneAtToString.isEmpty()) {
            try {
                doneAtTo = OffsetDateTime.parse(doneAtToString);
            } catch (DateTimeParseException e) {
                doneAtTo = null;
            }
        }

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Activity> query = cb.createQuery(Activity.class);
        Root<Activity> root = query.from(Activity.class);

        Join<Activity, ActivityType> activityTypeJoin = root.join("activityType");

        Predicate predicate = cb.conjunction();

        if (activityType != null && !activityType.isEmpty()) {
            predicate = cb.and(predicate, cb.equal(activityTypeJoin.get("name"), activityType));
        }

        if (doneAtFrom != null) {
            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("doneAt"), doneAtFrom));
        }

        if (doneAtTo != null) {
            predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("doneAt"), doneAtTo));
        }

        if (caloriesBurnedMin != -1) {
            Predicate caloriesBurnedMinPredicate = cb.greaterThanOrEqualTo(
                cb.prod(root.get("durationInMinutes"), activityTypeJoin.get("caloriesPerMinute")), caloriesBurnedMin);
            
            predicate = cb.and(predicate, caloriesBurnedMinPredicate);
        }

        if (caloriesBurnedMax != -1) {
            Predicate caloriesBurnedMaxPredicate = cb.lessThanOrEqualTo(
                cb.prod(root.get("durationInMinutes"), activityTypeJoin.get("caloriesPerMinute")), caloriesBurnedMax);
            
            predicate = cb.and(predicate, caloriesBurnedMaxPredicate);
        }

        query.where(predicate);

        List<Activity> result = entityManager.createQuery(query)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .getResultList();
        
        return result.stream().map(this::mapToGetResponse).toList();
    }

    public ActivityPersistResponse update(Long activityId, ActivityUpdateRequestBody activityRequestBody) throws ActivityNotFoundException, ActivityTypeNotFoundException {
        int counter = 0;
        Activity savedActivity = activityRepository.findById(activityId).orElseThrow(() -> new ActivityNotFoundException());

        ActivityType correspondingType = savedActivity.getActivityType();
        if (
            activityRequestBody.getActivityType() != null && 
            !activityRequestBody.getActivityType().isEmpty() &&
            !savedActivity.getActivityType().getName().equals(activityRequestBody.getActivityType())) {
            correspondingType = activityTypeRepository.findByName(activityRequestBody.getActivityType()).orElseThrow(() -> new ActivityTypeNotFoundException());
            savedActivity.setActivityType(correspondingType);
            counter++;
        }
        
        if (activityRequestBody.getDoneAt() != null) {
            OffsetDateTime doneAtFrom = null;
            try {
                doneAtFrom = OffsetDateTime.parse(activityRequestBody.getDoneAt());
                savedActivity.setDoneAt(doneAtFrom);
                counter++;
            } catch (DateTimeParseException e) {
                doneAtFrom = null;
            }
        }

        int durationInMinutes = savedActivity.getDurationInMinutes();
        if (activityRequestBody.getDurationInMinutes() != null) {
            durationInMinutes = activityRequestBody.getDurationInMinutes();
            savedActivity.setDurationInMinutes(durationInMinutes);
            counter++;
        }
        savedActivity.setCaloriesBurned(
            durationInMinutes * correspondingType.getCaloriesPerMinute()
        );
        
        
        if (counter > 0) {
            Activity updatedActivity = activityRepository.save(savedActivity);
            return mapToPersistResponse(updatedActivity);
        }
        return mapToPersistResponse(savedActivity);
    }

    public boolean delete(Long activityId) throws ActivityNotFoundException {
        if (!activityRepository.existsById(activityId)) {
            throw new ActivityNotFoundException();
        }
        activityRepository.deleteById(activityId);
        return true;
    }

    private ActivityGetResponse mapToGetResponse(Activity activity) {
        return new ActivityGetResponse(
            activity.getId().toString(),
            activity.getActivityType().getName(),
            activity.getDoneAt().toString(),
            activity.getDurationInMinutes(),
            activity.getCaloriesBurned(),
            activity.getCreatedAt().toString()
        );
    }

    private ActivityPersistResponse mapToPersistResponse(Activity activity) {
        return new ActivityPersistResponse(
            activity.getId().toString(),
            activity.getActivityType().getName(),
            activity.getDoneAt().toString(),
            activity.getDurationInMinutes(),
            activity.getCaloriesBurned(),
            activity.getCreatedAt().toString(),
            activity.getUpdatedAt().toString()
        );
    }

}
