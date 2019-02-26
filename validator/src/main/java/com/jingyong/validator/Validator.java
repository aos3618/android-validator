package com.jingyong.validator;

import com.jingyong.validator.checker.FieldCheckerFactory;
import com.jingyong.validator.checker.ParameterCheckerFactory;
import com.jingyong.validator.format.CheckField;
import com.jingyong.validator.format.Email;
import com.jingyong.validator.format.Mobile;
import com.jingyong.validator.format.base.Pattern;
import com.jingyong.validator.format.base.Size;

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
                if (Utils.isValidator(annotation)) {
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
                    Mobile mobile;
                    Email email;
                    Pattern pattern;
                    if ((mobile = Utils.getMobile(field)) != null) {
                        if (!FieldCheckerFactory.newMobileFieldChecker(field, mobile, object, prividerContent).check()) {
                            return false;// Return false will block all next steps;
                        }
                    } else if ((email = Utils.getEmail(field)) != null) {
                        if (!FieldCheckerFactory.newEmailFieldChecker(field, email, object, prividerContent).check()) {
                            return false;// Return false will block all next steps;
                        }
                    } else if ((pattern = Utils.getPattern(field)) != null) {
                        if (!FieldCheckerFactory.newPatternFieldChecker(field, pattern, object, prividerContent).check()) {
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
            if (Utils.isValidator(annotation)) {
                if (nullValue) {
                    return false;
                }

                if ((Utils.isSize(annotation))) {
                    Size size = (Size) annotation;
                    if (!ParameterCheckerFactory.newSizeParameterChecker(type, name, object, size, prividerContent).check()) {
                        return false;// Return false will block all next steps;
                    }
                } else if (Utils.isPattern(annotation)) {
                    Pattern pattern = (Pattern) annotation;
                    if (!ParameterCheckerFactory.newPatternParameterChecker(type, name, object, pattern, prividerContent).check()) {
                        return false;// Return false will block all next steps;
                    }
                }
            }
        }

        return true;
    }

}
