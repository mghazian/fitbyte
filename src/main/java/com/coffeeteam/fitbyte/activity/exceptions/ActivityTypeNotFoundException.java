package com.coffeeteam.fitbyte.activity.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ActivityTypeNotFoundException extends Exception {
    @Override
    public String getMessage() {
        return "activity type not found";
    }
}
