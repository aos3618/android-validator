package com.jingyong.validator;

import android.content.res.Resources;

import com.jingyong.validator.rule.IRuleProvider;
import com.jingyong.validator.rule.IWarningProvider;

/**
 * Created by AoS on 2019/2/25 0025.
 */

public class PrividerContent {

    private IRuleProvider ruleProvider;
    private Resources resources;
    private IWarningProvider warningProvider;

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public IRuleProvider getRuleProvider() {
        return ruleProvider;
    }

    public void setRuleProvider(IRuleProvider ruleProvider) {
        this.ruleProvider = ruleProvider;
    }

    public IWarningProvider getWarningProvider() {
        return warningProvider;
    }

    public void setWarningProvider(IWarningProvider warningProvider) {
        this.warningProvider = warningProvider;
    }
}
