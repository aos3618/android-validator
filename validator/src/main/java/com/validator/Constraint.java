package com.validator;

//
// Created by AoS on 2019/2/26.
//

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface Constraint {

    Class value();
}
