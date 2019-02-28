package com.validator.demo;

import com.validator.rule.IRuleValidator;

//
// Created by AoS on 2019/2/26.
//
public class CustomRule implements IRuleValidator<CustomeValidator> {   //need implement IRuleValidator and step1 Annotation as generics

    @Override
    public void initialize(CustomeValidator customeValidator, Object s) {  //init
    }

    @Override
    public boolean isValid() {   // how to valid the rule
        return true;
    }

    @Override
    public void showWarning() {   //how to show warning

    }
}
