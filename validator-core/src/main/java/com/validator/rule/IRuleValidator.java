package com.validator.rule;

//
// Created by AoS on 2019/2/26.
//
public interface IRuleValidator<R> {
    void initialize(R r, Object t);

    boolean isValid();

    void showWarning();
}
