package com.validator.demo;

import android.app.Application;
import android.widget.Toast;

import com.validator.Validator;
import com.validator.ValidatorConfig;
import com.validator.rule.IRuleProvider;
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
                        .setWarningProvider(new WarningProvider())  //custom how to show warning, default is Log
//                        .setRuleProvider(new RuleProvider())        //custom the check rule
        );
    }


    class WarningProvider implements IWarningProvider {

        @Override
        public void show(String s) {
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        }
    }

//    class RuleProvider implements IRuleProvider {
//
//        @Override
//        public boolean isMobile(String s) {
//            return false;
//        }
//
//        @Override
//        public boolean isEmail(String s) {
//            return false;
//        }
//
//        @Override
//        public boolean isPattern(String s, String regex) {
//            return false;
//        }
//
//        @Override
//        public boolean sizeIn(int min, int max, String s) {
//            return false;
//        }
//
//        @Override
//        public boolean sizeIn(int min, int max, Integer integer) {
//            return false;
//        }
//
//        @Override
//        public boolean isBlank(String s) {
//            return false;
//        }
//
//        @Override
//        public boolean isBlank(int size) {
//            return false;
//        }
//
//        @Override
//        public boolean isNull(Object object) {
//            return object == null;
//        }
//
//        @Override
//        public boolean lessThan(int value, int max) {
//            return false;
//        }
//
//        @Override
//        public boolean moreThan(int value, int min) {
//            return false;
//        }
//    }
}
