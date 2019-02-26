package com.validator;

import android.util.Log;

import com.validator.format.Check;
import com.validator.format.CheckField;
import com.validator.format.Email;
import com.validator.format.Mobile;
import com.validator.format.base.Max;
import com.validator.format.base.Min;
import com.validator.format.base.NotBlank;
import com.validator.format.base.Pattern;
import com.validator.format.base.Size;

import java.lang.annotation.Annotation;

//
// Created by AoS on 2019/2/22.
//
public class Utils {

    public static final String TAG = "JYValidator";

    public static void Log(String message) {
        Log.e(TAG, message);
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
                || annotation instanceof NotBlank
                || isCustomValidator(annotation);
    }

    public static boolean isCustomValidator(Annotation annotation) {
        Constraint constraint = annotation.annotationType().getAnnotation(Constraint.class);
        return constraint != null;
    }

    public static boolean isCheckField(Annotation annotation) {
        return annotation instanceof CheckField;
    }

    public static boolean isMobile(Annotation annotation) {
        return annotation instanceof Mobile;
    }

    public static boolean isEmail(Annotation annotation) {
        return annotation instanceof Email;
    }

    public static boolean isNotBlank(Annotation annotation) {
        return annotation instanceof NotBlank;
    }

    public static boolean isMax(Annotation annotation) {
        return annotation instanceof Max;
    }

    public static boolean isMin(Annotation annotation) {
        return annotation instanceof Min;
    }

    public static boolean isSize(Annotation annotation) {
        return annotation instanceof Size;
    }

    public static boolean isPattern(Annotation annotation) {
        return annotation instanceof Pattern;
    }
}
