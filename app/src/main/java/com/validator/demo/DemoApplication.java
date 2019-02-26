package com.validator.demo;

import android.app.Application;
import android.widget.Toast;

import com.validator.Validator;
import com.validator.ValidatorConfig;
import com.validator.rule.IWarningProvider;

//
// Created by AoS on 2019/2/26.
//
public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Validator.init(
                ValidatorConfig
                        .newInstance()
                        .setApplication(this)
                        .setWarningProvider(new WarningProvider())
        );
    }


    class WarningProvider implements IWarningProvider {

        @Override
        public void show(String s) {
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        }
    }
}
