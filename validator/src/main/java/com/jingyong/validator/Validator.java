package com.jingyong.validator;

import com.jingyong.validator.checker.CheckerFactory;
import com.jingyong.validator.format.CheckField;
import com.jingyong.validator.format.MobileField;
import com.jingyong.validator.rule.DefaultRule;
import com.jingyong.validator.rule.IValidatorRule;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

//
// Created by AoS on 2019/2/22.
//
public class Validator {

    private static HashMap<Class, ClassContent> sCheckCache = new HashMap<>();

    private static IValidatorRule rule = new DefaultRule();

    private void init(IValidatorRule rule) {

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

    public static boolean checkMethod(Object object, Method method) {
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
                    if ((mobileField = Utils.getMobileField(field)) != null) {
                        if (!CheckerFactory.getMobileFieldChecker(field, mobileField, object, rule).check()) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

}
