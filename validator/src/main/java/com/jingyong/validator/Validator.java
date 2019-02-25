package com.jingyong.validator;

import android.app.Application;

import com.jingyong.validator.checker.CheckerFactory;
import com.jingyong.validator.format.CheckField;
import com.jingyong.validator.format.EmailField;
import com.jingyong.validator.format.MobileField;
import com.jingyong.validator.format.PatternField;
import com.jingyong.validator.format.PatternParameter;
import com.jingyong.validator.format.SizeParameter;
import com.jingyong.validator.rule.DefaultRule;
import com.jingyong.validator.rule.IRuleProvider;

import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

//
// Created by AoS on 2019/2/22.
//
public class Validator {

    private static HashMap<Class, ClassContent> sCheckCache = new HashMap<>();

    private static IRuleProvider rule;
    private static Application application;

    Validator(ValidatorBuilder builder) {
        application = builder.getApplication();
        rule = builder.getRuleProvider();

    }

    public static void inject(Object object) {
        //获取到类中所有的成员变量
        Class clz = object.getClass();
        Field[] declaredFields = clz.getDeclaredFields();
        Utils.Log("inject:" + clz.getCanonicalName());
        ClassContent classContent = null;

        for (Field field : declaredFields) {
            //得到成员变量的注解

            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (Utils.isValidatorField(annotation)) {
                    if (classContent == null) {
                        classContent = new ClassContent(clz);
                    }
                    classContent.addField(field.getName(), field);
                    break;
                }
            }
        }

        Method[] methods = clz.getDeclaredMethods();

        for (Method method : methods) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (Utils.isCheckMethod(annotation)) {
                    if (classContent == null) {
                        classContent = new ClassContent(clz);
                    }
                    classContent.addMethod(method);
                    break;
                }
            }
        }

        if (classContent != null) {
            sCheckCache.put(clz, classContent);
        }

    }

    public static boolean checkMethod(Object object, MethodSignature methodSignature, Object[] argsValues) {

        Method method = methodSignature.getMethod();
        CheckField annotation = method.getAnnotation(CheckField.class);

        boolean checkResult = true;
        if (annotation != null) {
            checkResult = checkMethodAnnotation(object, method, annotation);
        }

        checkResult = checkResult && checkParameter(object, methodSignature, argsValues);

        return checkResult;
    }

    private static boolean checkMethodAnnotation(Object object, Method method, Annotation annotations) {
        Class clz = object.getClass();
        Utils.Log("checkMethod:" + clz.getCanonicalName());
        if (sCheckCache.get(clz) == null) {
            Utils.Log("Inject the class First");
            return false;
        }
        ClassContent classContent = sCheckCache.get(clz);
        CheckField annotation = method.getAnnotation(CheckField.class);
        if (annotation != null) {
            String[] values = annotation.value();

            for (String value : values) {
                if (classContent.getFields().get(value) == null) {
                    Utils.Log("No field named : " + value + " for method :" + method.getName() + " of Class : " + clz.getCanonicalName());
                } else {
                    Field field = classContent.getFields().get(value);
                    MobileField mobileField;
                    EmailField emailField;
                    PatternField patternField;
                    if ((mobileField = Utils.getMobileField(field)) != null) {
                        if (!CheckerFactory.getMobileFieldChecker(field, mobileField, object, rule).check()) {
//                            return false;// Return false will block all next steps;
                        }
                    } else if ((emailField = Utils.getEmailField(field)) != null) {
                        if (!CheckerFactory.getEmailFieldChecker(field, emailField, object, rule).check()) {
//                            return false;// Return false will block all next steps;
                        }
                    } else if ((patternField = Utils.getPatternField(field)) != null) {
                        if (!CheckerFactory.getPatternFieldChecker(field, patternField, object, rule).check()) {
//                            return false;// Return false will block all next steps;
                        }
                    }
                }
            }
        }

        return true;
    }

    private static boolean checkParameter(Object object, MethodSignature methodSignature, Object[] argValues) {

        String[] argNames = methodSignature.getParameterNames();
        if (argNames == null || argNames.length <= 0) {
            return true;
        }

        Method method = methodSignature.getMethod();
        int argsCount = argNames.length;
        Annotation[][] annotations = method.getParameterAnnotations();
        Class[] argsTypes = methodSignature.getParameterTypes();
        for (int i = 0; i < argsCount; ++i) {
            checkParameterAnnotation(method, argsTypes[i], argNames[i], argValues[i], annotations[i]);
        }

        return true;
    }

    private static boolean checkParameterAnnotation(Method method, Class type, String name, Object object, Annotation[] annotations) {
        if (annotations == null) {
            return true;
        }

        boolean nullValue = object == null;

        for (Annotation annotation : annotations) {
            if (Utils.isValidatorParameter(annotation)) {
                if (nullValue) {
                    return false;
                }

                if ((Utils.isSizeParameter(annotation))) {
                    SizeParameter sizeParameter = (SizeParameter) annotation;
                    if (!CheckerFactory.getSizeParameterChecker(type, name, object, sizeParameter, rule).check()) {

                    }
                } else if (Utils.isPatternParameter(annotation)) {
                    PatternParameter patternParameter = (PatternParameter) annotation;
                    if (!CheckerFactory.getPatternParameterChecker(type, name, object, patternParameter, rule).check()) {

                    }
                }
            }
        }

        return true;
    }

}
