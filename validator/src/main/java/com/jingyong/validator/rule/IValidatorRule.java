package com.jingyong.validator.rule;

/**
 * Created by AoS on 2019/2/22 0022.
 */

public interface IValidatorRule {

    boolean isMobile(String s);

    boolean isEmail(String s);

    boolean sizeIn(int min, int max, String s);

    boolean sizeIn(int min, int max, Integer integer);

}
