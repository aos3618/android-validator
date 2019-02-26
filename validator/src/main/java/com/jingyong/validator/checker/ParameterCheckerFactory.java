package com.jingyong.validator.checker;

import com.jingyong.validator.PrividerContent;
import com.jingyong.validator.format.base.Pattern;
import com.jingyong.validator.format.base.Size;
import com.jingyong.validator.rule.IRuleProvider;
import com.jingyong.validator.rule.IWarningProvider;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Created by AoS on 2019/2/22 0022.
 */

public class ParameterCheckerFactory {

    public static IChecker newSizeParameterChecker(Class type, String name, Object value, Size size, PrividerContent prividerContent) {
        return new SizeParameterChecker(type, name, value, size, prividerContent);
    }

    public static IChecker newPatternParameterChecker(Class type, String name, Object value, Pattern pattern, PrividerContent prividerContent) {
        return new PatternParameterChecker(type, name, value, pattern, prividerContent);
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
            IWarningProvider warningProvider = prividerContent.getWarningProvider();
            if (value instanceof String) {
                if (!rule.sizeIn(size.min(), size.max(), String.valueOf(value))) {
                    warningProvider.show(size.warning());
                    return false;
                }

            } else if (value instanceof Collection) {
                if (!rule.sizeIn(size.min(), size.max(), getCollectionSize(value))) {
                    warningProvider.show(size.warning());
                    return false;
                }
            } else if (!rule.sizeIn(size.min(), size.max(), getTextValue(value))) {
                warningProvider.show(size.warning());
                return false;
            } else {
                return false;
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
            IWarningProvider warningProvider = prividerContent.getWarningProvider();
            if (value instanceof String) {
                if (!rule.isPattern(String.valueOf(value), pattern.value())) {
                    warningProvider.show(pattern.warning());
                    return false;
                }

            } else {
                if (!rule.isPattern(getTextValue(value), pattern.value())) {
                    warningProvider.show(pattern.warning());
                    return false;
                }
            }
            return true;
        }

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
