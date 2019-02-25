package com.jingyong.validator.rule;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by AoS on 2019/2/22 0022.
 */

public class DefaultRule implements IRuleProvider {
    @Override
    public boolean isMobile(String s) {
        return s != null && s.length() == 11 && s.startsWith("1");
    }

    @Override
    public boolean isEmail(String s) {
        return s.contains("@");
    }

    @Override
    public boolean isPattern(String s, String regex) {
        return match(s, regex);
    }

    @Override
    public boolean sizeIn(int min, int max, String s) {
        return s.length() > min && s.length() < max;
    }

    @Override
    public boolean sizeIn(int min, int max, Integer integer) {
        return integer > min && integer < max;
    }

    private static boolean match(String str, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}
