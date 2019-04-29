package com.validator.format;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

//
// Created by AoS on 2019/2/25.
//
@Documented
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface Email {
    String warning() default "";

    int warningId() default 0;
}
