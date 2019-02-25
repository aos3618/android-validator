package com.jingyong.validator.handler;

import com.jingyong.validator.Utils;
import com.jingyong.validator.Validator;

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

    private static final String CHECK = "execution(@com.jingyong.validator.format.Check * *(..))";

    private static final String CHECK_ANN = CHECK;

    @Before(CHECK_ANN)
    public void before(JoinPoint point) {
        Utils.Log("@Before");
    }

    @Around(CHECK_ANN)
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        Utils.Log("@Around:" + joinPoint.getTarget().getClass().getCanonicalName());

        if (joinPoint.getSignature() instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            if (Validator.checkMethod(joinPoint.getTarget(), methodSignature, joinPoint.getArgs())) {
                joinPoint.proceed();
            }
        } else {
            Utils.Log("@Check Must use at a Method");
        }
    }
}
