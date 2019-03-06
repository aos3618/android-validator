package com.validator.demo;

import android.util.Log;
import android.widget.Toast;

import com.validator.rule.IRuleValidator;

//
// Created by AoS on 2019/3/6.
//
public class LoginRule implements IRuleValidator<LoginValiadtor> {
    UserInfo userInfo;

    @Override
    public void initialize(LoginValiadtor loginValiadtor, Object t) {
        if (t instanceof UserInfo) {
            userInfo = (UserInfo) t;
        }
    }

    @Override
    public boolean isValid() {
        return userInfo != null && userInfo.isLogin();
    }

    @Override
    public void showWarning() {
//        Toast.makeText(context, "请登录", Toast.LENGTH_SHORT);
        Log.d("TAG", "请登录");
    }
}
