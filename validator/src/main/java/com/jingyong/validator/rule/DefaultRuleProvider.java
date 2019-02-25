package com.jingyong.validator.rule;

import android.util.Log;

import com.jingyong.validator.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by AoS on 2019/2/22 0022.
 */

public class DefaultRuleProvider implements IRuleProvider {
    @Override
    public boolean isMobile(String s) {
        return s != null && s.length() == 11 && s.startsWith("1");
    }

    @Override
    public boolean isEmail(String s) {
        return s != null && s.contains("@");
    }

    @Override
    public boolean isPattern(String s, String regex) {
        return s != null && regex != null && match(s, regex);
    }

    @Override
    public boolean sizeIn(int min, int max, String s) {
        return s != null && s.length() >= min && s.length() <= max;
    }

    @Override
    public boolean sizeIn(int min, int max, Integer integer) {
        return integer != null && integer > min && integer < max;
    }

    private static boolean match(String str, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}
