package com.coffeeteam.fitbyte.activity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
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
    private EntityManager entityManager;

    public List<Activity> findActivities(
        int limit,
        int offset,
        String activityType,
        LocalDateTime doneAtFrom,
        LocalDateTime doneAtTo,
        int caloriesBurnedMin,
        int caloriesBurnedMax
    ) {
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
        
        return result;
    }

}
