package com.jingyong.validator.format.base;

//
// Created by AoS on 2019/2/22.
//

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)

public @interface Max {

    int value();

    String warning() default "";
}
