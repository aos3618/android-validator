package com.jingyong.validator.checker;

import com.jingyong.validator.PrividerContent;
import com.jingyong.validator.format.field.EmailField;
import com.jingyong.validator.format.field.MobileField;
import com.jingyong.validator.format.field.PatternField;
import com.jingyong.validator.format.parameter.PatternParameter;
import com.jingyong.validator.format.parameter.SizeParameter;
import com.jingyong.validator.rule.IRuleProvider;
import com.jingyong.validator.rule.IWarningProvider;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Created by AoS on 2019/2/22 0022.
 */

public class CheckerFactory {


    public static IChecker getMobileFieldChecker(Field field, MobileField mobileField, Object object, PrividerContent prividerContent) {
        return new MobileFieldChecker(field, mobileField, object, prividerContent);
    }

    public static IChecker getEmailFieldChecker(Field field, EmailField emailField, Object object, PrividerContent prividerContent) {
        return new EmailFieldChecker(field, emailField, object, prividerContent);
    }

    public static IChecker getPatternFieldChecker(Field field, PatternField patternField, Object object, PrividerContent prividerContent) {
        return new PatternFieldChecker(field, patternField, object, prividerContent);
    }

    public static IChecker getSizeParameterChecker(Class type, String name, Object value, SizeParameter sizeParameter, PrividerContent prividerContent) {
        return new SizeParameterChecker(type, name, value, sizeParameter, prividerContent);
    }

    public static IChecker getPatternParameterChecker(Class type, String name, Object value, PatternParameter patternParameter, PrividerContent prividerContent) {
        return new PatternParameterChecker(type, name, value, patternParameter, prividerContent);
    }

    static class MobileFieldChecker implements IChecker {

        private Field field;
        private MobileField mobileField;
        private Object object;
        private PrividerContent prividerContent;

        MobileFieldChecker(Field field, MobileField mobileField, Object object, PrividerContent prividerContent) {
            this.field = field;
            this.mobileField = mobileField;
            this.object = object;
            this.prividerContent = prividerContent;
        }

        @Override
        public boolean check() {
            IRuleProvider rule = prividerContent.getRuleProvider();
            IWarningProvider warningProvider = prividerContent.getWarningProvider();
            if (!rule.isMobile(getValue(field, object))) {
                warningProvider.show(mobileField.warning());
                return false;
            }

            return true;
        }
    }

    static class EmailFieldChecker implements IChecker {

        private Field field;
        private EmailField emailField;
        private Object object;
        private PrividerContent prividerContent;

        EmailFieldChecker(Field field, EmailField emailField, Object object, PrividerContent prividerContent) {
            this.field = field;
            this.emailField = emailField;
            this.object = object;
            this.prividerContent = prividerContent;
        }

        @Override
        public boolean check() {
            IRuleProvider rule = prividerContent.getRuleProvider();
            IWarningProvider warningProvider = prividerContent.getWarningProvider();
            if (!rule.isEmail(getValue(field, object))) {
                warningProvider.show(emailField.warning());
                return false;
            }
            return true;
        }
    }

    static class PatternFieldChecker implements IChecker {

        private Field field;
        private PatternField patternField;
        private Object object;
        private PrividerContent prividerContent;

        PatternFieldChecker(Field field, PatternField patternField, Object object, PrividerContent prividerContent) {
            this.field = field;
            this.patternField = patternField;
            this.object = object;
            this.prividerContent = prividerContent;
        }

        @Override
        public boolean check() {
            IRuleProvider rule = prividerContent.getRuleProvider();
            IWarningProvider warningProvider = prividerContent.getWarningProvider();
            if (!rule.isPattern(getValue(field, object), patternField.value())) {
                warningProvider.show(patternField.warning());
                return false;
            }

            return true;
        }
    }

    static class SizeParameterChecker implements IChecker {

        private Class type;
        private String name;
        private Object value;
        private SizeParameter sizeParameter;
        private PrividerContent prividerContent;

        SizeParameterChecker(Class type, String name, Object value, SizeParameter sizeParameter, PrividerContent prividerContent) {
            this.type = type;
            this.name = name;
            this.value = value;
            this.sizeParameter = sizeParameter;
            this.prividerContent = prividerContent;
        }

        @Override
        public boolean check() {
            IRuleProvider rule = prividerContent.getRuleProvider();
            IWarningProvider warningProvider = prividerContent.getWarningProvider();
            if (value instanceof String) {
                if (!rule.sizeIn(sizeParameter.min(), sizeParameter.max(), String.valueOf(value))) {
                    warningProvider.show(sizeParameter.warning());
                    return false;
                }

            } else if (value instanceof Collection) {
                if (!rule.sizeIn(sizeParameter.min(), sizeParameter.max(), getCollectionSize(value))) {
                    warningProvider.show(sizeParameter.warning());
                    return false;
                }
            } else {
                if (!rule.sizeIn(sizeParameter.min(), sizeParameter.max(), getTextValue(value))) {
                    warningProvider.show(sizeParameter.warning());
                    return false;
                }
            }
            return true;
        }
    }

    static class PatternParameterChecker implements IChecker {

        private Class type;
        private String name;
        private Object value;
        private PatternParameter patternParameter;
        private PrividerContent prividerContent;

        PatternParameterChecker(Class type, String name, Object value, PatternParameter patternParameter, PrividerContent prividerContent) {
            this.type = type;
            this.name = name;
            this.value = value;
            this.patternParameter = patternParameter;
            this.prividerContent = prividerContent;
        }

        @Override
        public boolean check() {
            IRuleProvider rule = prividerContent.getRuleProvider();
            IWarningProvider warningProvider = prividerContent.getWarningProvider();
            if (value instanceof String) {
                if (!rule.isPattern(String.valueOf(value), patternParameter.value())) {
                    warningProvider.show(patternParameter.warning());
                    return false;
                }

            } else {
                if (!rule.isPattern(getTextValue(value), patternParameter.value())) {
                    warningProvider.show(patternParameter.warning());
                    return false;
                }
            }
            return true;
        }

    }

    private static String getValue(Field field, Object object) {
        if (field.getType().equals(String.class)) {
            return getStringValue(field, object);
        } else {
            return getTextValue(field, object);
        }
    }

    private static String getStringValue(Field field, Object object) {
        field.setAccessible(true);
        try {
            return String.valueOf(field.get(object));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getTextValue(Field field, Object object) {
        Method getText;
        try {
            getText = field.getType().getDeclaredMethod("getText");
            Field superField = object.getClass().getDeclaredField(field.getName());
            superField.setAccessible(true);
            Object realField = superField.get(object);
            getText.setAccessible(true);
            return String.valueOf(getText.invoke(realField));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getTextValue(Object value) {
        Method getText;
        try {
            getText = value.getClass().getDeclaredMethod("getText");
            getText.setAccessible(true);
            return String.valueOf(getText.invoke(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int getCollectionSize(Object value) {
        Method size;
        try {
            size = value.getClass().getDeclaredMethod("size");
            size.setAccessible(true);
            return (int) size.invoke(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
