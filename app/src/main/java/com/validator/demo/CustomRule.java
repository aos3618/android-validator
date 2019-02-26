package com.validator.demo;

import com.jingyong.validator.Utils;
import com.jingyong.validator.rule.IRuleValidator;

//
// Created by AoS on 2019/2/26.
//
public class CustomRule implements IRuleValidator<CustomeAnn> {

    @Override
    public void initialize(CustomeAnn customeAnn, Object s) {
        Utils.Log("custom rule" + customeAnn.value() + " : " + s);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void showWarning() {

    }
}
