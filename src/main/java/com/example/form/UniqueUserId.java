package com.example.form;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueUserIdValidator.class)
public @interface UniqueUserId {
    String message() default "ユーザーIDが重複しています";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
