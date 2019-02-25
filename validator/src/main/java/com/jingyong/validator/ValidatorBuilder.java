package com.jingyong.validator;

import android.app.Application;
import android.content.res.Resources;

import com.jingyong.validator.rule.DefaultRuleProvider;
import com.jingyong.validator.rule.DefaultWarningProvider;
import com.jingyong.validator.rule.IRuleProvider;
import com.jingyong.validator.rule.IWarningProvider;

//
// Created by AoS on 2019/2/25.
//
public class ValidatorBuilder {

    private Application application;
    private IRuleProvider ruleProvider;
    private IWarningProvider warningProvider;
    private Resources resources;

    public ValidatorBuilder() {
        ruleProvider = new DefaultRuleProvider();
        warningProvider = new DefaultWarningProvider();
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public IWarningProvider getWarningProvider() {
        return warningProvider;
    }

    public ValidatorBuilder setWarningProvider(IWarningProvider warningProvider) {
        this.warningProvider = warningProvider;
        return this;
    }

    public Application getApplication() {
        return application;
    }

    public IRuleProvider getRuleProvider() {
        return ruleProvider;
    }

    public ValidatorBuilder setApplication(Application application) {
        this.application = application;
        this.resources = application.getResources();
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
