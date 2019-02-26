package com.jingyong.validator;

import android.util.Log;

import com.jingyong.validator.format.Check;
import com.jingyong.validator.format.CheckField;
import com.jingyong.validator.format.Email;
import com.jingyong.validator.format.Mobile;
import com.jingyong.validator.format.base.Max;
import com.jingyong.validator.format.base.Min;
import com.jingyong.validator.format.base.NotBlank;
import com.jingyong.validator.format.base.Pattern;
import com.jingyong.validator.format.base.Size;

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

    public static boolean isCheckMethod(Annotation annotation) {
        return annotation instanceof Check;
    }

    public static boolean isValidator(Annotation annotation) {
        return annotation instanceof Size
                || annotation instanceof Pattern
                || annotation instanceof Email
                || annotation instanceof Mobile
                || annotation instanceof Max
                || annotation instanceof Min
                || annotation instanceof NotBlank;
    }

    public static boolean isCheckField(Annotation annotation) {
        return annotation instanceof CheckField;
    }

    public static Mobile getMobile(Field field) {
        return field.getAnnotation(Mobile.class);
    }

    public static Email getEmail(Field field) {
        return field.getAnnotation(Email.class);
    }

    public static Pattern getPattern(Field field) {
        return field.getAnnotation(Pattern.class);
    }

    public static boolean isSize(Annotation annotation) {
        return annotation instanceof Size;
    }

    public static boolean isPattern(Annotation annotation) {
        return annotation instanceof Pattern;
    }
}
