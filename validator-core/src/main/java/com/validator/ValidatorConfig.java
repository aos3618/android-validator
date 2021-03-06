package com.validator;

import android.app.Application;
import android.content.res.Resources;

import com.validator.rule.DefaultRuleProvider;
import com.validator.rule.DefaultWarningProvider;
import com.validator.rule.IRuleProvider;
import com.validator.rule.IWarningProvider;

//
// Created by AoS on 2019/2/25.
//
public class ValidatorConfig {

    private Application application;
    private IRuleProvider ruleProvider;
    private IWarningProvider warningProvider;
    private Resources resources;

    public static ValidatorConfig newInstance() {
        return new ValidatorConfig();
    }

    public ValidatorConfig() {
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

    public ValidatorConfig setWarningProvider(IWarningProvider warningProvider) {
        this.warningProvider = warningProvider;
        return this;
    }

    public Application getApplication() {
        return application;
    }

    public IRuleProvider getRuleProvider() {
        return ruleProvider;
    }

    public ValidatorConfig setApplication(Application application) {
        this.application = application;
        this.resources = application.getResources();
        return this;
    }

    public ValidatorConfig setRuleProvider(IRuleProvider ruleProvider) {
        this.ruleProvider = ruleProvider;
        return this;
    }
}
