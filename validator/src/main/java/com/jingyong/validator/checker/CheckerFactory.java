package com.jingyong.validator.checker;

import com.jingyong.validator.Utils;
import com.jingyong.validator.format.EmailField;
import com.jingyong.validator.format.MobileField;
import com.jingyong.validator.format.PatternField;
import com.jingyong.validator.rule.IRuleProvider;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by AoS on 2019/2/22 0022.
 */

public class CheckerFactory {


    public static IChecker getMobileFieldChecker(Field field, MobileField mobileField, Object object, IRuleProvider rule) {
        return new MobileFieldChecker(field, mobileField, object, rule);
    }

    public static IChecker getEmailFieldChecker(Field field, EmailField emailField, Object object, IRuleProvider rule) {
        return new EmailFieldChecker(field, emailField, object, rule);
    }

    public static IChecker getPatternFieldCheck(Field field, PatternField patternField, Object object, IRuleProvider rule) {
        return new PatternFieldChecker(field, patternField, object, rule);
    }

    static class MobileFieldChecker implements IChecker {

        private Field field;
        private MobileField mobileField;
        private Object object;
        private IRuleProvider rule;

        MobileFieldChecker(Field field, MobileField mobileField, Object object, IRuleProvider rule) {
            this.field = field;
            this.mobileField = mobileField;
            this.object = object;
            this.rule = rule;
        }

        @Override
        public boolean check() {
            if (!rule.isMobile(getValue(field, object))) {
                Utils.Log(field.getName() + mobileField.warning());
                return false;
            } else {
                Utils.Log(field.getName() + " is a right pattern format");
            }

            return true;
        }
    }

    static class EmailFieldChecker implements IChecker {

        private Field field;
        private EmailField emailField;
        private Object object;
        private IRuleProvider rule;

        EmailFieldChecker(Field field, EmailField emailField, Object object, IRuleProvider rule) {
            this.field = field;
            this.emailField = emailField;
            this.object = object;
            this.rule = rule;
        }

        @Override
        public boolean check() {

            if (!rule.isEmail(getValue(field, object))) {
                Utils.Log(field.getName() + emailField.warning());
                return false;
            } else {
                Utils.Log(field.getName() + " is a right pattern format");
            }

            return true;
        }
    }

    static class PatternFieldChecker implements IChecker {

        private Field field;
        private PatternField patternField;
        private Object object;
        private IRuleProvider rule;

        PatternFieldChecker(Field field, PatternField patternField, Object object, IRuleProvider rule) {
            this.field = field;
            this.patternField = patternField;
            this.object = object;
            this.rule = rule;
        }

        @Override
        public boolean check() {

            if (!rule.isPattern(getValue(field, object), patternField.value())) {
                Utils.Log(field.getName() + patternField.warning());
                return false;
            } else {
                Utils.Log(field.getName() + " is a right pattern format");
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
}
