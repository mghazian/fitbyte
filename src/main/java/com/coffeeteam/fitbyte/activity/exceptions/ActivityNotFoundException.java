package com.coffeeteam.fitbyte.activity.exceptions;

public class ActivityNotFoundException extends Exception{
    @Override
    public String getMessage() {
        return "activity not found";
    }
}
