package com.validator.checker;

import com.validator.PrividerContent;
import com.validator.format.Email;
import com.validator.format.Mobile;
import com.validator.format.base.Max;
import com.validator.format.base.Min;
import com.validator.format.base.NotBlank;
import com.validator.format.base.Pattern;
import com.validator.format.base.Size;
import com.validator.rule.IRuleProvider;
import com.validator.rule.IWarningProvider;

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

    public static IChecker newNotBlankFieldChecker(Field field, NotBlank notBlank, Object object, PrividerContent prividerContent) {
        return new NotBlankFieldChecker(field, notBlank, object, prividerContent);
    }

    public static IChecker newMaxFieldChecker(Field field, Max max, Object object, PrividerContent prividerContent) {
        return new MaxFieldChecker(field, max, object, prividerContent);
    }

    public static IChecker newMinFieldChecker(Field field, Min min, Object object, PrividerContent prividerContent) {
        return new MinFieldChecker(field, min, object, prividerContent);
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

    static class NotBlankFieldChecker implements IFieldChecker {
        private Field field;
        private NotBlank notBlank;
        private Object object;
        private PrividerContent prividerContent;

        NotBlankFieldChecker(Field field, NotBlank notBlank, Object object, PrividerContent prividerContent) {
            this.field = field;
            this.notBlank = notBlank;
            this.object = object;
            this.prividerContent = prividerContent;
        }

        @Override
        public boolean check() {

            IRuleProvider rule = prividerContent.getRuleProvider();
            IWarningProvider warningProvider = prividerContent.getWarningProvider();

            if ((String.class).equals(field.getType())) {
                if (rule.isBlank(getStringValue(field, object))) {
                    warningProvider.show(notBlank.warning());
                    return false;
                }
            } else {
                int size;
                String value;
                if (checkNull(field, object)) {
                    warningProvider.show(notBlank.warning());
                    return false;
                } else if ((size = getCollectionSize(field, object)) != -1) {
                    if (rule.isBlank(size)) {
                        warningProvider.show(notBlank.warning());
                        return false;
                    }
                } else if ((value = getTextValue(field, object)) != null) {
                    if (rule.isBlank(value)) {
                        warningProvider.show(notBlank.warning());
                        return false;
                    }
                } else {
                    return false;
                }

            }
            return true;
        }
    }

    static class MaxFieldChecker implements IFieldChecker {

        private Field field;
        private Max max;
        private Object object;
        private PrividerContent prividerContent;

        MaxFieldChecker(Field field, Max max, Object object, PrividerContent prividerContent) {
            this.field = field;
            this.max = max;
            this.object = object;
            this.prividerContent = prividerContent;
        }

        @Override
        public boolean check() {
            IRuleProvider rule = prividerContent.getRuleProvider();
            IWarningProvider warningProvider = prividerContent.getWarningProvider();
            Object obj = getObject(field, object);

            if (obj == null) {
                warningProvider.show(max.warning());
                return false;
            } else {
                try {
                    int v = (int) obj;
                    if (rule.moreThan(v, max.value())) {
                        warningProvider.show(max.warning());
                        return false;
                    }
                } catch (Exception e) {
                    return false;
                }
            }

            return true;
        }
    }

    static class MinFieldChecker implements IFieldChecker {

        private Field field;
        private Min min;
        private Object object;
        private PrividerContent prividerContent;

        MinFieldChecker(Field field, Min min, Object object, PrividerContent prividerContent) {
            this.field = field;
            this.min = min;
            this.object = object;
            this.prividerContent = prividerContent;
        }

        @Override
        public boolean check() {
            IRuleProvider rule = prividerContent.getRuleProvider();
            IWarningProvider warningProvider = prividerContent.getWarningProvider();
            Object obj = getObject(field, object);

            if (obj == null) {
                warningProvider.show(min.warning());
                return false;
            } else {
                try {
                    int v = (int) obj;
                    if (rule.lessThan(v, min.value())) {
                        warningProvider.show(min.warning());
                        return false;
                    }
                } catch (Exception e) {
                    return false;
                }
            }

            return true;
        }
    }

    private static boolean checkNull(Field field, Object object) {
        return getObject(field, object) == null;
    }

    public static Object getObject(Field field, Object object) {
        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
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
