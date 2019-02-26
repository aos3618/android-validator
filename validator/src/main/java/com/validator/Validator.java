package com.validator;

import com.validator.checker.FieldCheckerFactory;
import com.validator.checker.ParameterCheckerFactory;
import com.validator.format.CheckField;
import com.validator.format.Email;
import com.validator.format.Mobile;
import com.validator.format.base.Max;
import com.validator.format.base.Min;
import com.validator.format.base.NotBlank;
import com.validator.format.base.Pattern;
import com.validator.format.base.Size;
import com.validator.rule.IRuleValidator;

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

                    for (Annotation ann : field.getDeclaredAnnotations()) {

                        if ((Utils.isMobile(ann))) {
                            Mobile mobile = (Mobile) ann;
                            if (!FieldCheckerFactory.newMobileFieldChecker(field, mobile, object, prividerContent).check()) {
                                return false;// Return false will block all next steps;
                            }
                        } else if (Utils.isPattern(ann)) {
                            Pattern pattern = (Pattern) ann;
                            if (!FieldCheckerFactory.newPatternFieldChecker(field, pattern, object, prividerContent).check()) {
                                return false;// Return false will block all next steps;
                            }
                        } else if (Utils.isEmail(ann)) {
                            Email email = (Email) ann;
                            if (!FieldCheckerFactory.newEmailFieldChecker(field, email, object, prividerContent).check()) {
                                return false;// Return false will block all next steps;
                            }
                        } else if (Utils.isNotBlank(ann)) {
                            NotBlank notBlank = (NotBlank) ann;
                            if (!FieldCheckerFactory.newNotBlankFieldChecker(field, notBlank, object, prividerContent).check()) {
                                return false;// Return false will block all next steps;
                            }
                        } else if (Utils.isSize(ann)) {
                            Size size = (Size) ann;
                            if (!FieldCheckerFactory.newSizeFieldChecker(field, size, object, prividerContent).check()) {
                                return false;// Return false will block all next steps;
                            }
                        } else if (Utils.isMax(ann)) {
                            Max max = (Max) ann;
                            if (!FieldCheckerFactory.newMaxFieldChecker(field, max, object, prividerContent).check()) {
                                return false;
                            }
                        } else if (Utils.isMin(ann)) {
                            Min min = (Min) ann;
                            if (!FieldCheckerFactory.newMinFieldChecker(field, min, object, prividerContent).check()) {
                                return false;
                            }
                        } else {
                            Constraint constraint = ann.annotationType().getAnnotation(Constraint.class);
                            if (constraint != null) {
                                try {
                                    if (!checkCustomValidator(ann, constraint, field, object)) {
                                        return false;
                                    }
                                } catch (InstantiationException | IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                            }
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

    private static boolean checkParameterAnnotation(Method method, Class type, String name, Object value, Annotation[] annotations) {
        if (annotations == null) {
            return true;
        }

        for (Annotation annotation : annotations) {
            if (Utils.isValidator(annotation)) {

                if ((Utils.isSize(annotation))) {
                    Size size = (Size) annotation;
                    if (!ParameterCheckerFactory.newSizeParameterChecker(type, name, value, size, prividerContent).check()) {
                        return false;// Return false will block all next steps;
                    }
                } else if (Utils.isPattern(annotation)) {
                    Pattern pattern = (Pattern) annotation;
                    if (!ParameterCheckerFactory.newPatternParameterChecker(type, name, value, pattern, prividerContent).check()) {
                        return false;// Return false will block all next steps;
                    }
                } else if (Utils.isMobile(annotation)) {
                    Mobile mobile = (Mobile) annotation;
                    if (!ParameterCheckerFactory.newMobileParameterChecker(type, name, value, mobile, prividerContent).check()) {
                        return false;// Return false will block all next steps;
                    }
                } else if (Utils.isEmail(annotation)) {
                    Email email = (Email) annotation;
                    if (!ParameterCheckerFactory.newEmailFieldChecker(type, name, value, email, prividerContent).check()) {
                        return false;// Return false will block all next steps;
                    }
                } else if (Utils.isNotBlank(annotation)) {
                    NotBlank notBlank = (NotBlank) annotation;
                    if (!ParameterCheckerFactory.newNotBlankFieldChecker(type, name, value, notBlank, prividerContent).check()) {
                        return false;// Return false will block all next steps;
                    }
                } else if (Utils.isMax(annotation)) {
                    Max max = (Max) annotation;
                    if (!ParameterCheckerFactory.newMaxFieldChecker(type, name, value, max, prividerContent).check()) {
                        return false;// Return false will block all next steps;
                    }
                } else if (Utils.isMin(annotation)) {
                    Min min = (Min) annotation;
                    if (!ParameterCheckerFactory.newMinFieldChecker(type, name, value, min, prividerContent).check()) {
                        return false;// Return false will block all next steps;
                    }
                } else {
                    Constraint constraint = annotation.annotationType().getAnnotation(Constraint.class);
                    if (constraint != null) {
                        try {
                            if (!checkCustomValidator(annotation, constraint, value)) {
                                return false;
                            }
                        } catch (InstantiationException | IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }

        return true;
    }

    private static boolean checkCustomValidator(Annotation annotation, Constraint constraint, Field field, Object clz) throws InstantiationException, IllegalAccessException {
        return checkCustomValidator(annotation, constraint, FieldCheckerFactory.getObject(field, clz));
    }

    private static boolean checkCustomValidator(Annotation annotation, Constraint constraint, Object object) throws InstantiationException, IllegalAccessException {
        try {
            IRuleValidator iRuleValidator = (IRuleValidator) constraint.value().newInstance();
            iRuleValidator.initialize(annotation, object);
            if (!iRuleValidator.isValid()) {
                iRuleValidator.showWarning();
                return false;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(annotation.annotationType()
                    + "\n" + e.getMessage());
        }
        return true;
    }
}
