package com.jingyong.validator;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.jingyong.validator.checker.CheckerFactory;
import com.jingyong.validator.format.field.CheckField;
import com.jingyong.validator.format.field.EmailField;
import com.jingyong.validator.format.field.MobileField;
import com.jingyong.validator.format.field.PatternField;
import com.jingyong.validator.format.parameter.PatternParameter;
import com.jingyong.validator.format.parameter.SizeParameter;
import com.jingyong.validator.rule.IRuleProvider;
import com.jingyong.validator.rule.IWarningProvider;

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

    private static PrividerContent prividerContent;

    Validator(ValidatorBuilder builder) {
        prividerContent = new PrividerContent();
        prividerContent.setRuleProvider(builder.getRuleProvider());
        prividerContent.setWarningProvider(builder.getWarningProvider());
        prividerContent.setResources(builder.getResources());
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
                        if (!CheckerFactory.getMobileFieldChecker(field, mobileField, object, prividerContent).check()) {
                            return false;// Return false will block all next steps;
                        }
                    } else if ((emailField = Utils.getEmailField(field)) != null) {
                        if (!CheckerFactory.getEmailFieldChecker(field, emailField, object, prividerContent).check()) {
                            return false;// Return false will block all next steps;
                        }
                    } else if ((patternField = Utils.getPatternField(field)) != null) {
                        if (!CheckerFactory.getPatternFieldChecker(field, patternField, object, prividerContent).check()) {
                            return false;// Return false will block all next steps;
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
            if (!checkParameterAnnotation(method, argsTypes[i], argNames[i], argValues[i], annotations[i])) {
                return false;
            }
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
                    if (!CheckerFactory.getSizeParameterChecker(type, name, object, sizeParameter, prividerContent).check()) {
                        return false;// Return false will block all next steps;
                    }
                } else if (Utils.isPatternParameter(annotation)) {
                    PatternParameter patternParameter = (PatternParameter) annotation;
                    if (!CheckerFactory.getPatternParameterChecker(type, name, object, patternParameter, prividerContent).check()) {
                        return false;// Return false will block all next steps;
                    }
                }
            }
        }

        return true;
    }

}
