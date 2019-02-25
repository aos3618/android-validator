package com.jingyong.validator;

import android.util.Log;

import com.jingyong.validator.format.Check;
import com.jingyong.validator.format.field.CheckField;
import com.jingyong.validator.format.field.EmailField;
import com.jingyong.validator.format.field.MaxField;
import com.jingyong.validator.format.field.MinField;
import com.jingyong.validator.format.field.MobileField;
import com.jingyong.validator.format.field.NotBlankField;
import com.jingyong.validator.format.field.PatternField;
import com.jingyong.validator.format.field.SizeField;
import com.jingyong.validator.format.parameter.EmailParameter;
import com.jingyong.validator.format.parameter.MaxParameter;
import com.jingyong.validator.format.parameter.MinParameter;
import com.jingyong.validator.format.parameter.MobileParameter;
import com.jingyong.validator.format.parameter.NotBlankParameter;
import com.jingyong.validator.format.parameter.PatternParameter;
import com.jingyong.validator.format.parameter.SizeParameter;

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
                || annotation instanceof PatternField
                || annotation instanceof SizeField
                || annotation instanceof MaxField
                || annotation instanceof MinField
                || annotation instanceof NotBlankField;
    }

    public static boolean isCheckMethod(Annotation annotation) {
        return annotation instanceof Check;
    }

    public static boolean isValidatorParameter(Annotation annotation) {
        return annotation instanceof SizeParameter
                || annotation instanceof PatternParameter
                || annotation instanceof EmailParameter
                || annotation instanceof MobileParameter
                || annotation instanceof MaxParameter
                || annotation instanceof MinParameter
                || annotation instanceof NotBlankParameter;
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

    public static boolean isPatternParameter(Annotation annotation) {
        return annotation instanceof PatternParameter;
    }
}
