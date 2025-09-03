package com.coffeeteam.fitbyte.activity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActivityController {

    @GetMapping("/v1/activity")
    public String get(
        @RequestParam(name = "limit", required = false, defaultValue = "5") int limit,
        @RequestParam(name = "offset", required = false, defaultValue =  "0") int offset,
        @RequestParam(name = "activityType", required = false, defaultValue = "") String activityType,
        @RequestParam(name = "doneAtFrom", required = false) String doneAtFrom,
        @RequestParam(name = "doneAtTo", required = false) String doneAtTo,
        @RequestParam(name = "caloriesBurnedMin", required = false, defaultValue = "-1") int caloriesBurnedMin,
        @RequestParam(name = "caroiesBurnedMax", required = false, defaultValue = "-1") int caloriesBurnedMax 
    ) {
        return String.format("%d %d %s %s %s %d %d", limit, offset, activityType, doneAtFrom, doneAtTo, caloriesBurnedMin, caloriesBurnedMax);
    }
}
