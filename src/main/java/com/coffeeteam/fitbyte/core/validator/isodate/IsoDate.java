package com.coffeeteam.fitbyte.core.validator.isodate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsoDateValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsoDate {
    String message() default "must be a valid ISO date string";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
