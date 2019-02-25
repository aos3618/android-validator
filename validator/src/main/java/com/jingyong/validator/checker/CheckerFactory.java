package com.jingyong.validator.checker;

import com.jingyong.validator.Utils;
import com.jingyong.validator.format.EmailField;
import com.jingyong.validator.format.MobileField;
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
            if (field.getType().equals(String.class)) {
                field.setAccessible(true);
                try {
                    if (!rule.isMobile(String.valueOf(field.get(object)))) {
                        Utils.Log(field.getName() + mobileField.warning());
                        return false;
                    } else {
                        Utils.Log(field.getName() + " is a Mobile format");
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Method getText = field.getType().getDeclaredMethod("getText");
                    Field superField = object.getClass().getDeclaredField(field.getName());
                    superField.setAccessible(true);
                    Object realField = superField.get(object);
                    getText.setAccessible(true);
                    if (!rule.isMobile(String.valueOf(getText.invoke(realField)))) {
                        Utils.Log(field.getName() + mobileField.warning());
                    } else {
                        Utils.Log(field.getName() + " is a Mobile format");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
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
            if (field.getType().equals(String.class)) {
                field.setAccessible(true);
                try {
                    if (!rule.isEmail(String.valueOf(field.get(object)))) {
                        Utils.Log(field.getName() + emailField.warning());
                        return false;
                    } else {
                        Utils.Log(field.getName() + " is a Email format");
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Method getText = field.getType().getDeclaredMethod("getText");
                    Field superField = object.getClass().getDeclaredField(field.getName());
                    superField.setAccessible(true);
                    Object realField = superField.get(object);
                    getText.setAccessible(true);
                    if (!rule.isEmail(String.valueOf(getText.invoke(realField)))) {
                        Utils.Log(field.getName() + emailField.warning());
                    } else {
                        Utils.Log(field.getName() + " is a Email format");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
    }
}
