package com.coffeeteam.fitbyte.activity;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coffeeteam.fitbyte.activity.dto.ActivityPersistResponse;
import com.coffeeteam.fitbyte.activity.dto.ActivityUpdateRequestBody;
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
    ) {
        ActivityType correspondingType = activityTypeRepository.findByName(activityRequestBody.getActivityType()).orElseThrow(() -> new EntityNotFoundException());
        int caloriesBurned = correspondingType.getCaloriesPerMinute() * activityRequestBody.getDurationInMinutes();
        LocalDateTime doneAt = LocalDateTime.parse(activityRequestBody.getDoneAt());
        
        Activity saved = activityRepository.save(new Activity(
            correspondingType,
            doneAt,
            activityRequestBody.getDurationInMinutes(),
            caloriesBurned
        ));
        return new ActivityPersistResponse(
            saved.toString(), 
            saved.getActivityType().getName(), 
            saved.getDoneAt().toString(), 
            saved.getDurationInMinutes(), 
            saved.getCaloriesBurned(),
            saved.getCreatedAt().toString(), 
            saved.getUpdatedAt().toString());
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
        LocalDateTime doneAtFrom = null;
        if (doneAtFromString != null && !doneAtFromString.isEmpty()) {
            try {
                doneAtFrom = LocalDateTime.parse(doneAtFromString);
            } catch (DateTimeParseException e) {
                doneAtFrom = null;
            }
        }

        LocalDateTime doneAtTo = null;
        if (doneAtToString != null && !doneAtToString.isEmpty()) {
            try {
                doneAtTo = LocalDateTime.parse(doneAtToString);
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

    public ActivityPersistResponse update(Long activityId, ActivityUpdateRequestBody activityRequestBody) {
        int counter = 0;
        Activity savedActivity = activityRepository.findById(activityId).orElseThrow(() -> new EntityNotFoundException("activityId is not found"));

        ActivityType correspondingType = savedActivity.getActivityType();
        if (
            activityRequestBody.getActivityType() != null && 
            !activityRequestBody.getActivityType().isEmpty() &&
            !savedActivity.getActivityType().getName().equals(activityRequestBody.getActivityType())) {
            correspondingType = activityTypeRepository.findByName(activityRequestBody.getActivityType()).orElseThrow(() -> new EntityNotFoundException());
            savedActivity.setActivityType(correspondingType);
            counter++;
        }
        
        if (activityRequestBody.getDoneAt() != null && !activityRequestBody.getDoneAt().isEmpty()) {
            LocalDateTime doneAt = LocalDateTime.parse(activityRequestBody.getDoneAt());
            savedActivity.setDoneAt(doneAt);
            counter++;
        }

        int durationInMinutes = savedActivity.getDurationInMinutes();
        if (activityRequestBody.getDurationInMinutes() != 0) {
            durationInMinutes = correspondingType.getCaloriesPerMinute() * activityRequestBody.getDurationInMinutes();
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

    public boolean delete(Long activityId) {
        if (!activityRepository.existsById(activityId)) {
            throw new EntityNotFoundException("activityId is not found");
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
