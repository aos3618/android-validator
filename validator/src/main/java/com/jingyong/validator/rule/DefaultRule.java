package com.jingyong.validator.rule;

/**
 * Created by AoS on 2019/2/22 0022.
 */

public class DefaultRule implements IValidatorRule {
    @Override
    public boolean isMobile(String s) {
        return s != null && s.length() == 11 && s.startsWith("1");
    }

    @Override
    public boolean isEmail(String s) {
        return s.contains("@");
    }

    @Override
    public boolean sizeIn(int min, int max, String s) {
        return s.length() > min && s.length() < max;
    }

    @Override
    public boolean sizeIn(int min, int max, Integer integer) {
        return integer > min && integer < max;
    }
}
