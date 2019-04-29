package com.validator.checker;

import android.content.res.Resources;

import com.validator.PrividerContent;
import com.validator.format.Email;
import com.validator.format.Mobile;
import com.validator.format.base.Max;
import com.validator.format.base.Min;
import com.validator.format.base.NotBlank;
import com.validator.format.base.NotNull;
import com.validator.format.base.Pattern;
import com.validator.format.base.Size;
import com.validator.rule.IRuleProvider;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Created by AoS on 2019/2/22 0022.
 */

public class ParameterCheckerFactory {
    public static IChecker newMobileParameterChecker(Class type, String name, Object value, Mobile mobile, PrividerContent prividerContent) {
        return new MobileParameterChecker(type, name, value, mobile, prividerContent);
    }

    public static IChecker newEmailFieldChecker(Class type, String name, Object value, Email email, PrividerContent prividerContent) {
        return new EmailParameterChecker(type, name, value, email, prividerContent);
    }

    public static IChecker newSizeParameterChecker(Class type, String name, Object value, Size size, PrividerContent prividerContent) {
        return new SizeParameterChecker(type, name, value, size, prividerContent);
    }

    public static IChecker newPatternParameterChecker(Class type, String name, Object value, Pattern pattern, PrividerContent prividerContent) {
        return new PatternParameterChecker(type, name, value, pattern, prividerContent);
    }

    public static IChecker newNotBlankFieldChecker(Class type, String name, Object value, NotBlank notBlank, PrividerContent prividerContent) {
        return new NotBlankParameterChecker(type, name, value, notBlank, prividerContent);
    }

    public static IChecker newNotNullFieldChecker(Class type, String name, Object value, NotNull notnull, PrividerContent prividerContent) {
        return new NotNullParameterChecker(type, name, value, notnull, prividerContent);
    }

    public static IChecker newMaxFieldChecker(Class type, String name, Object value, Max max, PrividerContent prividerContent) {
        return new MaxParameterChecker(type, name, value, max, prividerContent);
    }

    public static IChecker newMinFieldChecker(Class type, String name, Object value, Min min, PrividerContent prividerContent) {
        return new MinparameterChecker(type, name, value, min, prividerContent);
    }

    static class MobileParameterChecker implements IParameterChecker {

        private Class type;
        private String name;
        private Object value;
        private Mobile mobile;
        private PrividerContent prividerContent;

        MobileParameterChecker(Class type, String name, Object value, Mobile mobile, PrividerContent prividerContent) {
            this.type = type;
            this.name = name;
            this.value = value;
            this.mobile = mobile;
            this.prividerContent = prividerContent;
        }

        @Override
        public boolean check() {
            IRuleProvider rule = prividerContent.getRuleProvider();
            if (checkNull(value)) {
                showWarning(prividerContent, mobile.warningId(), mobile.warning());
                return false;
            }
            if (value instanceof String) {
                if (!rule.isMobile(String.valueOf(value))) {
                    showWarning(prividerContent, mobile.warningId(), mobile.warning());
                    return false;
                }

            } else if (!rule.isMobile(getTextValue(value))) {
                showWarning(prividerContent, mobile.warningId(), mobile.warning());
                return false;
            } else {
                return false;
            }

            return true;
        }
    }

    static class EmailParameterChecker implements IParameterChecker {

        private Class type;
        private String name;
        private Object value;
        private Email email;
        private PrividerContent prividerContent;

        EmailParameterChecker(Class type, String name, Object value, Email email, PrividerContent prividerContent) {
            this.type = type;
            this.name = name;
            this.value = value;
            this.email = email;
            this.prividerContent = prividerContent;
        }

        @Override
        public boolean check() {
            IRuleProvider rule = prividerContent.getRuleProvider();
            if (checkNull(value)) {
                showWarning(prividerContent, email.warningId(), email.warning());
                return false;
            }
            if (value instanceof String) {
                if (!rule.isEmail(String.valueOf(value))) {
                    showWarning(prividerContent, email.warningId(), email.warning());
                    return false;
                }

            } else if (!rule.isEmail(getTextValue(value))) {
                showWarning(prividerContent, email.warningId(), email.warning());
                return false;
            } else {
                return false;
            }

            return true;
        }
    }

    static class SizeParameterChecker implements IParameterChecker {

        private Class type;
        private String name;
        private Object value;
        private Size size;
        private PrividerContent prividerContent;

        SizeParameterChecker(Class type, String name, Object value, Size size, PrividerContent prividerContent) {
            this.type = type;
            this.name = name;
            this.value = value;
            this.size = size;
            this.prividerContent = prividerContent;
        }

        @Override
        public boolean check() {
            IRuleProvider rule = prividerContent.getRuleProvider();
            if (checkNull(value)) {
                showWarning(prividerContent, size.warningId(), size.warning());
                return false;
            }
            if (value instanceof String) {
                if (!rule.sizeIn(size.min(), size.max(), String.valueOf(value))) {
                    showWarning(prividerContent, size.warningId(), size.warning());
                    return false;
                }

            } else if (value instanceof Collection) {
                if (!rule.sizeIn(size.min(), size.max(), getCollectionSize((Collection) value))) {
                    showWarning(prividerContent, size.warningId(), size.warning());
                    return false;
                }
            } else if (!rule.sizeIn(size.min(), size.max(), getTextValue(value))) {
                showWarning(prividerContent, size.warningId(), size.warning());
                return false;
            } else {
                return getCollectionSize(value) > 0;
            }

            return true;
        }
    }

    static class PatternParameterChecker implements IParameterChecker {

        private Class type;
        private String name;
        private Object value;
        private Pattern pattern;
        private PrividerContent prividerContent;

        PatternParameterChecker(Class type, String name, Object value, Pattern pattern, PrividerContent prividerContent) {
            this.type = type;
            this.name = name;
            this.value = value;
            this.pattern = pattern;
            this.prividerContent = prividerContent;
        }

        @Override
        public boolean check() {
            IRuleProvider rule = prividerContent.getRuleProvider();
            if (checkNull(value)) {
                showWarning(prividerContent, pattern.warningId(), pattern.warning());
                return false;
            }
            if (value instanceof String) {
                if (!rule.isPattern(String.valueOf(value), pattern.value())) {
                    showWarning(prividerContent, pattern.warningId(), pattern.warning());
                    return false;
                }

            } else {
                if (!rule.isPattern(getTextValue(value), pattern.value())) {
                    showWarning(prividerContent, pattern.warningId(), pattern.warning());
                    return false;
                }
            }
            return true;
        }
    }

    static class NotBlankParameterChecker implements IParameterChecker {

        private Class type;
        private String name;
        private Object value;
        private NotBlank notBlank;
        private PrividerContent prividerContent;

        NotBlankParameterChecker(Class type, String name, Object value, NotBlank notBlank, PrividerContent prividerContent) {
            this.type = type;
            this.name = name;
            this.value = value;
            this.notBlank = notBlank;
            this.prividerContent = prividerContent;
        }

        @Override
        public boolean check() {
            IRuleProvider rule = prividerContent.getRuleProvider();
            if (checkNull(value)) {
                showWarning(prividerContent, notBlank.warningId(), notBlank.warning());
                return false;
            }
            if (value instanceof String) {
                if (rule.isBlank(String.valueOf(value))) {
                    showWarning(prividerContent, notBlank.warningId(), notBlank.warning());
                    return false;
                }

            } else if (value instanceof Collection) {
                if (rule.isBlank(getCollectionSize((Collection) value))) {
                    showWarning(prividerContent, notBlank.warningId(), notBlank.warning());
                    return false;
                }
            } else if (rule.isBlank(getTextValue(value))) {
                showWarning(prividerContent, notBlank.warningId(), notBlank.warning());
                return false;
            } else {
                return getCollectionSize(value) > 0;
            }

            return true;
        }
    }

    static class NotNullParameterChecker implements IParameterChecker {

        private Class type;
        private String name;
        private Object value;
        private NotNull notNull;
        private PrividerContent prividerContent;

        NotNullParameterChecker(Class type, String name, Object value, NotNull notNull, PrividerContent prividerContent) {
            this.type = type;
            this.name = name;
            this.value = value;
            this.notNull = notNull;
            this.prividerContent = prividerContent;
        }

        @Override
        public boolean check() {
            IRuleProvider rule = prividerContent.getRuleProvider();
            if (rule.isNull(value)) {
                showWarning(prividerContent, notNull.warningId(), notNull.warning());
                return false;
            }
            return true;
        }
    }

    static class MaxParameterChecker implements IParameterChecker {

        private Class type;
        private String name;
        private Object value;
        private Max max;
        private PrividerContent prividerContent;

        MaxParameterChecker(Class type, String name, Object value, Max max, PrividerContent prividerContent) {
            this.type = type;
            this.name = name;
            this.value = value;
            this.max = max;
            this.prividerContent = prividerContent;
        }

        @Override
        public boolean check() {
            IRuleProvider rule = prividerContent.getRuleProvider();
            if (checkNull(value)) {
                showWarning(prividerContent, max.warningId(), max.warning());
                return false;
            }
            try {
                int v = (int) value;
                if (rule.moreThan(v, max.value())) {
                    showWarning(prividerContent, max.warningId(), max.warning());
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
            return true;
        }
    }

    static class MinparameterChecker implements IParameterChecker {

        private Class type;
        private String name;
        private Object value;
        private Min min;
        private PrividerContent prividerContent;

        MinparameterChecker(Class type, String name, Object value, Min min, PrividerContent prividerContent) {
            this.type = type;
            this.name = name;
            this.value = value;
            this.min = min;
            this.prividerContent = prividerContent;
        }

        @Override
        public boolean check() {
            IRuleProvider rule = prividerContent.getRuleProvider();
            if (checkNull(value)) {
                showWarning(prividerContent, min.warningId(), min.warning());
                return false;
            }
            try {
                int v = (int) value;
                if (rule.lessThan(v, min.value())) {
                    showWarning(prividerContent, min.warningId(), min.warning());
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
            return true;
        }
    }

    private static boolean checkNull(Object value) {
        return value == null;
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

    private static int getCollectionSize(Collection value) {
        return value.size();
    }

    private static void showWarning(PrividerContent prividerContent, int id, String str) {
        if (0 != id) {
            if (null != prividerContent.getResources()) {
                Resources resources = prividerContent.getResources();
                prividerContent.getWarningProvider().show(resources.getString(id));
            }
        } else {
            prividerContent.getWarningProvider().show(str);
        }
    }
}
