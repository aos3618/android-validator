package com.validator.demo;

//
// Created by AoS on 2019/2/26.
//

import com.validator.Constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(LoginRule.class)   //Must have a Constraint Annotation，and define a check rule
public @interface LoginValiadtor {
    String value() default "LoginValidator";
}