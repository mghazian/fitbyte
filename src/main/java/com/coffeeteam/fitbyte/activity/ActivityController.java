package com.coffeeteam.fitbyte.activity;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coffeeteam.fitbyte.activity.dto.ActivityGetResponse;
import com.coffeeteam.fitbyte.activity.dto.ActivityPersistResponse;
import com.coffeeteam.fitbyte.activity.dto.ActivityUpdateRequestBody;
import com.coffeeteam.fitbyte.activity.exceptions.ActivityNotFoundException;
import com.coffeeteam.fitbyte.activity.exceptions.ActivityTypeNotFoundException;
import com.coffeeteam.fitbyte.activity.service.ActivityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;

import com.coffeeteam.fitbyte.activity.dto.ActivityCreateRequestBody;

@RestController
@RequestMapping("/v1")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

    @PostMapping("/activity")
    public ResponseEntity<ActivityPersistResponse> create(
        @Valid @RequestBody ActivityCreateRequestBody requestBody
    ) throws ActivityTypeNotFoundException {
        ActivityPersistResponse result = activityService.createActivity(requestBody);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/activity")
    public ResponseEntity<List<ActivityGetResponse>> get(
        @RequestParam(name = "limit", required = false, defaultValue = "5") int limit,
        @RequestParam(name = "offset", required = false, defaultValue =  "0") int offset,
        @RequestParam(name = "activityType", required = false, defaultValue = "") String activityType,
        @RequestParam(name = "doneAtFrom", required = false) String doneAtFrom,
        @RequestParam(name = "doneAtTo", required = false) String doneAtTo,
        @RequestParam(name = "caloriesBurnedMin", required = false, defaultValue = "-1") int caloriesBurnedMin,
        @RequestParam(name = "caloriesBurnedMax", required = false, defaultValue = "-1") int caloriesBurnedMax 
    ) {
        List<ActivityGetResponse> result = activityService.findActivities(limit, offset, activityType, doneAtFrom, doneAtTo, caloriesBurnedMin, caloriesBurnedMax);
        
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PatchMapping("/activity/{activityIdString}")
    public ResponseEntity<ActivityPersistResponse> update(
        @PathVariable("activityIdString") String activityIdString,
        @RequestBody JsonNode jsonNode) throws ActivityNotFoundException, ActivityTypeNotFoundException {
        Long activityId;
        try {
            activityId = Long.parseLong(activityIdString);
        } catch (NumberFormatException e) {
            throw new ActivityNotFoundException();
        }

        String doneAt = null;
        if (jsonNode.has("doneAt")) 
            doneAt = jsonNode.get("doneAt").isNull() 
            ? "" : 
            jsonNode.get("doneAt").asText();
        
        ActivityUpdateRequestBody requestBody;
        try {
            requestBody = objectMapper.treeToValue(jsonNode, ActivityUpdateRequestBody.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {
            throw e;
        }

        requestBody.setDoneAt(doneAt);
        Set<ConstraintViolation<ActivityUpdateRequestBody>> violations = validator.validate(requestBody);

        if (!violations.isEmpty()) {
            throw new IllegalArgumentException();
        }

        ActivityPersistResponse result = activityService.update(activityId, requestBody);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PatchMapping("/activity/")
    public ResponseEntity<?> patchWithoutId() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Activity ID is required in the path.");
    }

    @DeleteMapping("/activity/{activityIdString}")
    public ResponseEntity<Boolean> delete(@PathVariable("activityIdString") String activityIdString) throws ActivityNotFoundException {
        Long activityId;
        try {
            activityId = Long.parseLong(activityIdString);
        } catch (NumberFormatException e) {
            throw new ActivityNotFoundException();
        }
        boolean result = activityService.delete(activityId);
        
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/activity/")
    public ResponseEntity<?> deleteWithoutId() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Activity ID is required in the path.");
    }
}
