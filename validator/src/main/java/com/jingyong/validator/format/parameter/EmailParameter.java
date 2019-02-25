package com.jingyong.validator.format.parameter;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

//
// Created by AoS on 2019/2/25.
//

//TODO implement
@Documented
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface EmailParameter {
    String warning() default "";
}
