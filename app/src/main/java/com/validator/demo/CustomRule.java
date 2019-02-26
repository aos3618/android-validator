package com.validator.demo;

import com.validator.rule.IRuleValidator;

//
// Created by AoS on 2019/2/26.
//
public class CustomRule implements IRuleValidator<CustomeValidator> {

    @Override
    public void initialize(CustomeValidator customeValidator, Object s) {
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void showWarning() {

    }
}
