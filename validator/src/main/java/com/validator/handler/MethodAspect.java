package com.validator.handler;

import com.validator.Utils;
import com.validator.Validator;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

//
// Created by AoS on 2019/2/22.
//
@Aspect
public class MethodAspect {

    private static final String CHECK = "execution(@com.validator.format.Validate * *(..))";

    @Around(CHECK)
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {

        if (joinPoint.getSignature() instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            if (Validator.checkMethod(joinPoint.getTarget(), methodSignature, joinPoint.getArgs())) {
                joinPoint.proceed();
            }
        } else {
            throw new RuntimeException("@Validate Must use at a Method");
        }
    }
}
