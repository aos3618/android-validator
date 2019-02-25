package com.jingyong.validator;

import android.util.Log;
import android.util.Size;

import com.jingyong.validator.format.Check;
import com.jingyong.validator.format.CheckField;
import com.jingyong.validator.format.EmailField;
import com.jingyong.validator.format.MobileField;
import com.jingyong.validator.format.PatternField;
import com.jingyong.validator.format.SizeParameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

//
// Created by AoS on 2019/2/22.
//
public class Utils {

    public static final String TAG = "JYValidator";

    public static void Log(String message) {
        Log.d(TAG, message);
    }

    public static boolean isValidatorField(Annotation annotation) {
        return annotation instanceof MobileField
                || annotation instanceof EmailField
                || annotation instanceof PatternField;
    }

    public static boolean isCheckMethod(Annotation annotation) {
        return annotation instanceof Check;
    }

    public static boolean isValidatorParameter(Annotation annotation) {
        return annotation instanceof SizeParameter;
    }

    public static boolean isCheckField(Annotation annotation) {
        return annotation instanceof CheckField;
    }

    public static MobileField getMobileField(Field field) {
        return field.getAnnotation(MobileField.class);
    }

    public static EmailField getEmailField(Field field) {
        return field.getAnnotation(EmailField.class);
    }


    public static PatternField getPatternField(Field field) {
        return field.getAnnotation(PatternField.class);
    }


    public static boolean isSizeParameter(Annotation annotation) {
        return annotation instanceof SizeParameter;
    }
}
