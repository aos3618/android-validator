package com.validator.rule;

/**
 * Created by AoS on 2019/2/22 0022.
 */

public interface IRuleProvider {

    boolean isMobile(String s);

    boolean isEmail(String s);

    boolean isPattern(String s, String regex);

    boolean sizeIn(int min, int max, String s);

    boolean sizeIn(int min, int max, Integer integer);

    boolean isBlank(String s);

    boolean isBlank(int size);

    boolean isNull(Object object);

    boolean lessThan(int value, int max);

    boolean moreThan(int value, int min);

}
