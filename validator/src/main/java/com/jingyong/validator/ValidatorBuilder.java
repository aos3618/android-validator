package com.jingyong.validator;

import android.app.Application;

import com.jingyong.validator.rule.DefaultRule;
import com.jingyong.validator.rule.IRuleProvider;

//
// Created by AoS on 2019/2/25.
//
public class ValidatorBuilder {

    private Application application;
    private IRuleProvider ruleProvider;


    public ValidatorBuilder() {
        ruleProvider = new DefaultRule();
    }

    public Application getApplication() {
        return application;
    }

    public IRuleProvider getRuleProvider() {
        return ruleProvider;
    }

    public ValidatorBuilder setApplication(Application application) {
        this.application = application;
        return this;
    }

    public ValidatorBuilder setRuleProvider(IRuleProvider ruleProvider) {
        this.ruleProvider = ruleProvider;
        return this;
    }

    public Validator build() {
        return new Validator(this);
    }
}
