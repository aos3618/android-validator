package com.jingyong.validator.checker;

import com.jingyong.validator.PrividerContent;
import com.jingyong.validator.format.Email;
import com.jingyong.validator.format.Mobile;
import com.jingyong.validator.format.base.Pattern;
import com.jingyong.validator.format.base.Size;
import com.jingyong.validator.rule.IRuleProvider;
import com.jingyong.validator.rule.IWarningProvider;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by AoS on 2019/2/22 0022.
 */

public class FieldCheckerFactory {


    public static IChecker newMobileFieldChecker(Field field, Mobile mobile, Object object, PrividerContent prividerContent) {
        return new MobileFieldChecker(field, mobile, object, prividerContent);
    }

    public static IChecker newEmailFieldChecker(Field field, Email email, Object object, PrividerContent prividerContent) {
        return new EmailFieldChecker(field, email, object, prividerContent);
    }

    public static IChecker newPatternFieldChecker(Field field, Pattern pattern, Object object, PrividerContent prividerContent) {
        return new PatternFieldChecker(field, pattern, object, prividerContent);
    }

    public static IChecker newSizeFieldChecker(Field field, Size size, Object object, PrividerContent prividerContent) {
        return new SizeFieldChecker(field, size, object, prividerContent);
    }

    static class MobileFieldChecker implements IFieldChecker {

        private Field field;
        private Mobile mobile;
        private Object object;
        private PrividerContent prividerContent;

        MobileFieldChecker(Field field, Mobile mobile, Object object, PrividerContent prividerContent) {
            this.field = field;
            this.mobile = mobile;
            this.object = object;
            this.prividerContent = prividerContent;
        }

        @Override
        public boolean check() {
            IRuleProvider rule = prividerContent.getRuleProvider();
            IWarningProvider warningProvider = prividerContent.getWarningProvider();
            if (!rule.isMobile(getValue(field, object))) {
                warningProvider.show(mobile.warning());
                return false;
            }

            return true;
        }
    }

    static class EmailFieldChecker implements IFieldChecker {

        private Field field;
        private Email email;
        private Object object;
        private PrividerContent prividerContent;

        EmailFieldChecker(Field field, Email email, Object object, PrividerContent prividerContent) {
            this.field = field;
            this.email = email;
            this.object = object;
            this.prividerContent = prividerContent;
        }

        @Override
        public boolean check() {
            IRuleProvider rule = prividerContent.getRuleProvider();
            IWarningProvider warningProvider = prividerContent.getWarningProvider();
            if (!rule.isEmail(getValue(field, object))) {
                warningProvider.show(email.warning());
                return false;
            }
            return true;
        }
    }

    static class PatternFieldChecker implements IFieldChecker {

        private Field field;
        private Pattern pattern;
        private Object object;
        private PrividerContent prividerContent;

        PatternFieldChecker(Field field, Pattern pattern, Object object, PrividerContent prividerContent) {
            this.field = field;
            this.pattern = pattern;
            this.object = object;
            this.prividerContent = prividerContent;
        }

        @Override
        public boolean check() {
            IRuleProvider rule = prividerContent.getRuleProvider();
            IWarningProvider warningProvider = prividerContent.getWarningProvider();
            if (!rule.isPattern(getValue(field, object), pattern.value())) {
                warningProvider.show(pattern.warning());
                return false;
            }

            return true;
        }
    }

    static class SizeFieldChecker implements IFieldChecker {
        private Field field;
        private Size size;
        private Object object;
        private PrividerContent prividerContent;

        SizeFieldChecker(Field field, Size size, Object object, PrividerContent prividerContent) {
            this.field = field;
            this.size = size;
            this.object = object;
            this.prividerContent = prividerContent;
        }

        @Override
        public boolean check() {
            IRuleProvider rule = prividerContent.getRuleProvider();
            IWarningProvider warningProvider = prividerContent.getWarningProvider();
            if ((String.class).equals(field.getType())) {
                if (!rule.sizeIn(size.min(), size.max(), getStringValue(field, object))) {
                    warningProvider.show(size.warning());
                    return false;
                }
            } else {
                int size;
                String value;
                if ((size = getCollectionSize(field, object)) != -1) {
                    if (!rule.sizeIn(this.size.min(), this.size.max(), size)) {
                        warningProvider.show(this.size.warning());
                        return false;
                    }
                } else if ((value = getTextValue(field, object)) != null) {
                    if (!rule.sizeIn(this.size.min(), this.size.max(), value)) {
                        warningProvider.show(this.size.warning());
                        return false;
                    }
                } else {
                    return false;
                }

            }
            return true;
        }
    }

    private static String getValue(Field field, Object object) {
        if ((String.class).equals(field.getType())) {
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

    private static int getCollectionSize(Field field, Object object) {
        Method size;
        try {
            size = field.getType().getDeclaredMethod("size");
            Field superField = object.getClass().getDeclaredField(field.getName());
            superField.setAccessible(true);
            Object realField = superField.get(object);
            size.setAccessible(true);
            return (int) size.invoke(realField);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
